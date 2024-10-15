/*
 * Copyright 2024 Kinhiro Zix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package oyama.event.core

import net.jodah.typetools.TypeResolver
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import oyama.event.api.*
import oyama.event.api.CancelableEvent
import oyama.event.api.EventExceptionHandler
import oyama.event.api.EventHook
import oyama.event.api.EventPriority
import oyama.event.api.GenericEvent
import oyama.logging.LogMarkers
import java.lang.invoke.MethodHandles
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer

object EventManager {
    private val registers: ConcurrentHashMap<Any, MutableList<EventListenerWrapper>> = ConcurrentHashMap()
    private val listeners: MutableMap<Class<*>, EventListenerList> = Collections.synchronizedMap(IdentityHashMap())
    private val logger: Logger = LogManager.getLogger()
    private var exceptionHandler: EventExceptionHandler =
        EventExceptionHandler { _, throwable -> throwable.printStackTrace() }

    private val lookup: MethodHandles.Lookup = MethodHandles.lookup()

    @JvmStatic
    fun fire(event: Event): Boolean {
        for (listener in getListenerList(event.javaClass)) try {
            listener.fire(event)
        } catch (throwable: Throwable) {
            exceptionHandler.handle(event, throwable)
        }

        return (event is CancelableEvent) && event.isCanceled
    }

    @JvmStatic
    fun register(target: Any) {
        check(!registers.containsKey(target)) { "Event listener $target has already been registered!" }
        val isStatic = target is Class<*>
        val targetClass = if (isStatic) target else target.javaClass
        val listeners = arrayListOf<EventListenerWrapper>()
        for (method in targetClass.declaredMethods) {
            if (!method.isAnnotationPresent(EventHook::class.java)) continue
            method.isAccessible = true
            val hook = method.getAnnotation(EventHook::class.java)
            if (Modifier.isStatic(method.modifiers) == isStatic)
                listeners.add(registerListener(target, method, hook, isStatic))
            else {
                if (isStatic) throw IllegalStateException(
                    "Excepted @EventHook method $method to be static because register() was called with a class " +
                        "type. Either make the method static, or call register() with an instance of $targetClass."
                ) else throw IllegalStateException(
                    "Excepted @EventHook method $method to not be static because register() was called with an " +
                        "instance type. Either make the method non-static, or call register(${targetClass.simpleName}" +
                        ".class)."
                )
            }
        }

        check(listeners.isNotEmpty()) {
            "$targetClass has no @EventHook methods, but register was called anyway. " +
                "The event manager only recognizes listener that have the @EventHook annotation."
        }

        registers.computeIfAbsent(targetClass) { _ -> Collections.synchronizedList(listeners) }
    }

    @JvmStatic
    fun unregister(target: Any) {
        registers.remove(target)?.let {
            for (listener in it) getListenerList(listener.eventClass).unregister(listener)
        }
    }

    @JvmSynthetic
    @JvmName("addListenerKotlin")
    inline fun <reified E : Event> addListener(crossinline listener: (event: E) -> Unit) =
        addListener(EventPriority.NORMAL, E::class.java, false) { event -> listener(event) }

    @JvmStatic
    fun <E : Event> addListener(listener: Consumer<E>) =
        addListener(EventPriority.NORMAL, false, listener)

    @JvmStatic
    fun <E : Event> addListener(priority: EventPriority, listener: Consumer<E>) =
        addListener(priority, false, listener)

    @JvmStatic
    fun <E : Event> addListener(
        priority: EventPriority,
        receiveCanceled: Boolean,
        listener: Consumer<E>
    ) = @Suppress("UNCHECKED_CAST")
    addListener(priority, getEventClass(listener) as Class<E>, receiveCanceled, listener)

    @JvmStatic
    fun <E : Event> addListener(
        priority: EventPriority,
        eventClass: Class<E>,
        receiveCanceled: Boolean,
        listener: Consumer<E>
    ) {
        check(!registers.containsKey(listener)) { "Event listener $eventClass has already been registered!" }
        EventListenerWrapper(priority, eventClass, null, receiveCanceled, ConsumerEventListener(listener)).let {
            getListenerList(eventClass).register(it)
            registers.computeIfAbsent(eventClass) { _ -> Collections.synchronizedList(arrayListOf(it)) }
        }
    }

    @JvmSynthetic
    @JvmName("addGenericListenerKotlin")
    inline fun <reified E : GenericEvent<out T>, reified T> addGenericListener(
        crossinline listener: (event: E) -> Unit
    ) = addGenericListener(EventPriority.NORMAL, T::class.java, E::class.java, false) { event -> listener(event) }

    @JvmStatic
    fun <E : GenericEvent<out T>, T> addGenericListener(genericClass: Class<T>, listener: Consumer<E>) =
        addGenericListener(EventPriority.NORMAL, genericClass, false, listener)

    @JvmStatic
    fun <E : GenericEvent<out T>, T> addGenericListener(
        priority: EventPriority,
        genericClass: Class<T>,
        listener: Consumer<E>
    ) = addGenericListener(priority, genericClass, false, listener)

    @JvmStatic
    fun <E : GenericEvent<out T>, T> addGenericListener(
        priority: EventPriority,
        genericClass: Class<T>,
        receiveCanceled: Boolean,
        listener: Consumer<E>
    ) = @Suppress("UNCHECKED_CAST")
    addGenericListener(priority, genericClass, getEventClass(listener) as Class<E>, receiveCanceled, listener)

    @JvmStatic
    fun <E : GenericEvent<out T>, T> addGenericListener(
        priority: EventPriority,
        genericClass: Class<T>,
        eventClass: Class<E>,
        receiveCanceled: Boolean,
        listener: Consumer<E>
    ) {
        check(!registers.containsKey(listener)) { "Event listener $listener has already been registered!" }
        EventListenerWrapper(priority, genericClass, eventClass, receiveCanceled, ConsumerEventListener(listener)).let {
            getListenerList(eventClass).register(it)
            registers.computeIfAbsent(listener) { _ -> Collections.synchronizedList(arrayListOf(it)) }
        }
    }

    @JvmStatic
    fun handleException(handler: EventExceptionHandler) {
        exceptionHandler = handler
    }

    private fun <E : Event> getEventClass(listener: Consumer<E>): Class<*> =
        TypeResolver.resolveRawArgument(Consumer::class.java, listener.javaClass).also {
            check(it !== TypeResolver.Unknown::class.java) {
                logger.error(LogMarkers.EVENT, "Failed to resolve handler for '{}'.", listener.toString())
                "Failed to resolve consumer event type: $listener."
            }
        }

    private fun getListenerList(eventClass: Class<*>): EventListenerList =
        listeners[eventClass] ?: when {
            Modifier.isAbstract(eventClass.modifiers) -> throw IllegalArgumentException(
                "Cannot register listeners for abstract $eventClass. Register a listener to one of " +
                    "its subclass instead!"
            )

            Modifier.isAbstract(eventClass.superclass.modifiers) -> {
                var eventType = eventClass
                while (eventType !== Event::class.java) {
                    require(Modifier.isAbstract(eventType.superclass.modifiers)) {
                        "Abstract event $eventType has non-abstract superclass ${eventType.superclass}. " +
                            "The superclass must be made abstract."
                    }

                    eventType = eventType.superclass
                }

                EventListenerList().apply {
                    for ((key, value) in listeners)
                        if (key.isAssignableFrom(eventClass)) addParent(value)
                        else if (eventClass.isAssignableFrom(key)) addChild(value)
                }.also { listeners.computeIfAbsent(eventClass) { _ -> it } }
            }

            else -> throw IllegalArgumentException() // This might never be reached, but it is the desired outcome.
        }

    private fun registerListener(
        target: Any,
        method: Method,
        hook: EventHook,
        isStatic: Boolean
    ): EventListenerWrapper {
        require(Modifier.isPrivate(method.modifiers)) {
            "Method $method has @EventHook annotation, but it must be declared as private."
        }

        require(method.parameterCount == 1) {
            "Method $method has @EventHook annotation. It has ${method.parameterTypes.size} arguments, " +
                "but event handler methods require a single argument only."
        }

        val eventClass = method.parameterTypes[0]
        check(Event::class.java.isAssignableFrom(eventClass)) {
            "Method $method has @EventHook annotation, but takes an argument that is not an Event subtype: $eventClass."
        }

        var genericType: Type? = null
        if (GenericEvent::class.java.isAssignableFrom(eventClass)) {
            val type = method.genericParameterTypes[0]
            genericType = if (type is ParameterizedType) type.actualTypeArguments[0] else null
            if (genericType is ParameterizedType) genericType = genericType.rawType
        }

        val listener = try {
            lookup.unreflect(method).let { MethodHandleEventListener(if (isStatic) it else it.bindTo(target)) }
        } catch (e: Exception) {
            throw IllegalStateException(
                "Method $method has @EventHook annotation, but takes an argument that is not valid for " +
                    "this event listener $eventClass.",
                e
            )
        }

        return EventListenerWrapper(hook.priority, eventClass, genericType, hook.receiveCanceled, listener).also {
            getListenerList(eventClass).register(it)
        }
    }
}
