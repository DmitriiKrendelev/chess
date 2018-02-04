package org.dmkr.chess.common.cache;

import org.dmkr.chess.common.cache.CacheValue;
import org.dmkr.chess.common.cache.CacheReset;
import org.junit.Test;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.dmkr.chess.common.cache.CachableCreator.*;

public class CacheTest {

	public static interface SimpleInterface {
		public String a();
	
		public String b();
		
		public SimpleInterface clone();
	}
	
	@RequiredArgsConstructor
	@EqualsAndHashCode(of = "id")
	public static class SimpleInterfaceImpl implements SimpleInterface {
		private int countA = 0;	
		private final Object id;
		
		public SimpleInterfaceImpl() {
			this(new Object());
		}
		
		@CacheValue
		@Override
		public String a() {
			countA ++;
			return new String("a");
		}
		
		@CacheReset
		@Override
		public String b() {
			return "b";
		}
		
		public int getCountA() {
			return countA;
		}
		
		@Override
		public SimpleInterfaceImpl clone() {
			return new SimpleInterfaceImpl();
		}
	}
	
	@Test
	public void testIsCachableProxy() {
		assertFalse(isCachableProxy(new Object()));
		
		final SimpleInterfaceImpl instance = new SimpleInterfaceImpl();
		final SimpleInterfaceImpl proxy = createCachableProxy(instance);
		
		assertTrue(isCachableProxy(proxy));
		assertTrue(getOriginalObject(proxy) == instance);
	}
	
	@Test
	public void testCacheValueAndCachResetMethods() {
		final SimpleInterfaceImpl proxy = createCachableProxy(new SimpleInterfaceImpl());
		
		for (int i = 1; i < 100; i ++) {
			final String a = proxy.a();
			assertEquals(i, proxy.getCountA());
			
			assertTrue(a == proxy.a());
			assertTrue(a == proxy.a());
			
			assertEquals(i, proxy.getCountA());
			
			assertEquals("b", proxy.b());
		}
	}
	
	@Test
	public void equalsAndHashCode() {
		final SimpleInterfaceImpl instance = new SimpleInterfaceImpl(0);
		final SimpleInterfaceImpl proxy = createCachableProxy(instance);
		
		assertTrue(proxy.equals(proxy));
		assertTrue(proxy.equals(instance));
		assertTrue(proxy.equals(new SimpleInterfaceImpl(0)));
		
		assertEquals(proxy.hashCode(), proxy.hashCode());
		assertEquals(proxy.hashCode(), instance.hashCode());
		assertEquals(proxy.hashCode(), new SimpleInterfaceImpl(0).hashCode());
		
		assertFalse(proxy.equals(new SimpleInterfaceImpl(1)));
		assertNotEquals(proxy.hashCode(), new SimpleInterfaceImpl(1).hashCode());
	}
	
}
