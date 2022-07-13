package com.kh.spring.proxy.pattern;

/**
 * cosole 출력 결과
 * --------------------------
 * 	FooProxy#getName
 * 	///// Before 부가기능 /////
 * 	FooImpl#getName
 * 	///// After 부가기능 /////
 * 	이름 : Honggildong
 * --------------------------
 */
public class ProxyMain {

	// 의존주입객체
	// @Autowired 사용 시 Spring Container가 대신 의존주입하는 것
	Foo foo = new FooProxy(new FooImpl(), new Aspect());
	
	public static void main(String[] args) {
		new ProxyMain().test();
	}

	private void test() {
		System.out.println("이름 : " + foo.getName());
	}

}

interface Foo {
	String getName();
}

class FooImpl implements Foo {
	@Override
	public String getName() {
		System.out.println("FooImpl#getName");
		return "Honggildong";
	}
}

class FooProxy implements Foo {
	Foo foo;
	Aspect aspect;
	
	public FooProxy(Foo foo, Aspect aspect) {
		this.foo = foo;
		this.aspect = aspect;
	}
	
	@Override
	public String getName() {
		System.out.println("FooProxy#getName");
		aspect.beforeAdvice();
		
		String name = foo.getName();
		
		aspect.afterAdvice();
		
		return name;
	}
}

class Aspect {
	
	public void beforeAdvice() {
		System.out.println("///// Before 부가기능 /////");
	}
	public void afterAdvice() {
		System.out.println("///// After 부가기능 /////");
	}
}