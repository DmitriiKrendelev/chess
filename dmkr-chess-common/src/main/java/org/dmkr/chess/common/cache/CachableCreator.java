package org.dmkr.chess.common.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Method;
import static org.dmkr.chess.common.cache.Cache.cache;
import static org.dmkr.chess.common.collections.StreamUtils.toImmutableMap;
import static org.dmkr.chess.common.collections.StreamUtils.toImmutableSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CachableCreator<T>  {
	private static final String EQUALS = "equals";
	
	private static final Object[] NO_ARGS = new Object[0];
	private static final Class<?>[] ADDITIONAL_INTERFACES = new Class[]{ CachableProxyInternalInterface.class };
	private static final Method GET_ORIGINAL_OBJECT_METHOD = getOriginalObjectMethod();
	
	@SneakyThrows
	private static Method getOriginalObjectMethod() {
		return CachableProxyInternalInterface.class.getMethod("getOriginalObject", new Class<?>[]{});
	}
	
	public static interface CachableProxyInternalInterface {
		Object getOriginalObject();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T createCachableProxy(@NonNull T proxy) {
		if (isCachableProxy(proxy)) {
			return proxy;
		}
			
		final Class<?> classT = proxy.getClass();
		
		final List<Method> allMethods = (List<Method>) ReflectUtils.addAllMethods(classT, new ArrayList<>());
		
		final Map<Method, Cache<Object>> cachableMethods = allMethods.stream()
			.filter(method -> method.isAnnotationPresent(CacheValue.class))
			.filter(method -> method.getParameterTypes().length == 0)
			.distinct()
			.collect(toImmutableMap(Function.identity(), method -> cache(proxy, method, NO_ARGS)));

		
		final Set<Method> resetMethods = allMethods.stream()
			.filter(method -> method.isAnnotationPresent(CacheReset.class))
			.collect(toImmutableSet());
		
		final MethodInterceptor methodInterceptor = (obj, method, args, methodProxy) -> {
			if (GET_ORIGINAL_OBJECT_METHOD.equals(method)) {
				return proxy;
			}
			
			if (resetMethods.contains(method)) {
				cachableMethods.values().forEach(Cache::reset);
			}
			
			if (isEquals(method)) {
				return proxy.equals(getOriginalObject(args[0]));
			}
			
			final Cache<Object> cache = cachableMethods.get(method);
			final Object result = cache == null ? methodProxy.invoke(proxy, args) : cache.get();
		
			return result;
		};
		
		final Enhancer enhancer = new Enhancer();

		enhancer.setSuperclass(proxy.getClass());
		enhancer.setInterfaces(ADDITIONAL_INTERFACES);
		enhancer.setCallback(methodInterceptor);
		
		return (T) enhancer.create();
	}
	
	public static boolean isCachableProxy(Object o) {
		return CachableProxyInternalInterface.class.isInstance(o);
	}
	
	public static Object getOriginalObject(Object proxy) {
		return isCachableProxy(proxy) ? ((CachableProxyInternalInterface) proxy).getOriginalObject() : proxy;
	}
	
	private static boolean isEquals(Method method) {
		return EQUALS.equals(method.getName()) && method.getParameterTypes().length == 1;
	}
}
