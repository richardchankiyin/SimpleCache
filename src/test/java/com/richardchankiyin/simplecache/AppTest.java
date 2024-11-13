package com.richardchankiyin.simplecache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
	private final static Logger logger = LoggerFactory.getLogger(AppTest.class);
    /**
     * Test
     */
	@Test(expected=NullPointerException.class)
	public void testNullKey() {
		Cache<String,String> c = new CacheImpl<String,String>(s->"Input String:" + s);
		c.get(null);
	}
	@Test(expected=NullPointerException.class)
	public void testNullFunction() {
		new CacheImpl<String,String>(null);
	}
	@Test(expected=IllegalStateException.class)
	public void testFunctionFailed() {
		Cache<String,String> c = new CacheImpl<String,String>(s->{ if ("fake".equals(s)) { throw new IllegalStateException(); } return "Input String:" + s; });
		assertEquals("Input String:s", c.get("s"));
		c.get("fake");
	}
	@Test
	public void testFunctionComputeNull() {
		Cache<String,String> c = new CacheImpl<String,String>(s->null);
		assertNull(c.get("fake"));
	}
	
    @Test
    public void testCacheResultStringSuccess()
    {
        Cache<String,String> c = new CacheImpl<String,String>(s->"Input String:" + s);
        String c1 = c.get("input");
        String c2 = c.get("input");
        assertEquals("Input String:input", c1);
        assertTrue(c1 == c2);
        
        c1 = c.get("var");
        c2 = c.get("var");
        assertEquals("Input String:var", c1);
        assertTrue(c1 == c2);
    }
    @Test
    public void testCacheResultIntegerSuccess() {
    	Cache<Integer,Integer> c = new CacheImpl<Integer,Integer>(i->i*100);
    	Integer i1 = c.get(10);
    	Integer i2 = c.get(10);
    	assertTrue(1000 == i1);
    	assertTrue(i1 == i2);
    	
    	i1 = c.get(100);
    	i2 = c.get(100);
    	assertTrue(10000 == i1);
    	assertTrue(i1 == i2);
    }
    @Test
    public void testConcurrentCache() throws Exception{
    	Cache<String,String> c = new CacheImpl<String,String>(s->"Input String:" + s);
    	Runnable r = ()->{ assertEquals("Input String:var", c.get("var")); };
    	Thread t1 = new Thread(r);
    	Thread t2 = new Thread(r);
    	Thread t3 = new Thread(r);
    	Thread t4 = new Thread(r);
    	t1.start(); t2.start(); t3.start(); t4.start();
    	t1.join(); t2.join(); t3.join(); t4.join();
    	
    }
    
    @Test
    public void testFunctionTriggeredOnlyOnce() throws Exception {
    	class Count {
    		int count = 0;
    		public void add() { count++; }
    		public int get() {return count;}
    	}
    	
    	final Count count = new Count();
    	
    	Cache<String,String> c = new CacheImpl<String,String>(s->{logger.info("I am being triggered!!");count.add();return "process:"+s;});
    	Runnable r = ()->{ assertEquals("process:v", c.get("v")); };
    	Thread t1 = new Thread(r);
    	Thread t2 = new Thread(r);
    	Thread t3 = new Thread(r);
    	Thread t4 = new Thread(r);
    	t1.start(); t2.start(); t3.start(); t4.start();
    	t1.join(); t2.join(); t3.join(); t4.join();
    	
    	assertEquals(1,count.get());
    }

}
