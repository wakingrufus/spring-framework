package org.springframework.web.servlet.function

import org.springframework.beans.factory.BeanRegistrarDsl

/**
 * top-level function for creating routes with single parameter bean injection
 */
inline fun <reified P1 : Any> router(
	crossinline f: (dep1: P1) -> RouterFunctionDsl.() -> Unit
): BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.(P1) -> RouterFunction<ServerResponse> {
	return { p1 -> router { f(p1)(this) } }
}

/**
 * top-level function for creating routes with 2 parameter bean injection
 */
inline fun <reified P1 : Any, reified P2 : Any> router(
	crossinline f: (dep1: P1, dep2: P2) -> RouterFunctionDsl.() -> Unit
): BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.(P1, P2) -> RouterFunction<ServerResponse> {
	return { p1, p2 -> router { f(p1, p2)(this) } }
}

/**
 * top-level function for creating routes with 3 parameter bean injection
 */
inline fun <reified P1 : Any, reified P2 : Any, reified P3 : Any> router(
	crossinline f: (dep1: P1, dep2: P2, dep3: P3) -> RouterFunctionDsl.() -> Unit
): BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.(P1, P2, P3) -> RouterFunction<ServerResponse> {
	return { p1, p2, p3 -> router { f(p1, p2, p3)(this) } }
}

/**
 * convenience method for creating injected routes inline within the [BeanRegistrarDsl]
 */
inline fun <reified P1 : Any> BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.router(
	dep1: P1,
	crossinline f: (P1) -> (RouterFunctionDsl.() -> Unit)
): RouterFunction<ServerResponse> {
	return router(f)(this, dep1)
}

/**
 * convenience method for creating injected routes inline within the [BeanRegistrarDsl]
 */
inline fun <reified P1 : Any, reified P2 : Any> BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.routes(
	dep1: P1,
	dep2: P2,
	crossinline f: (P1, P2) -> RouterFunctionDsl.() -> Unit
): RouterFunction<ServerResponse> {
	return router(f)(this, dep1, dep2)
}

/**
 * convenience method for creating injected routes inline within the [BeanRegistrarDsl]
 */
inline fun <reified P1 : Any, reified P2 : Any, reified P3 : Any> BeanRegistrarDsl.SupplierContextDsl<RouterFunction<ServerResponse>>.routes(
	dep1: P1,
	dep2: P2,
	dep3: P3,
	crossinline f: (P1, P2, P3) -> RouterFunctionDsl.() -> Unit
): RouterFunction<ServerResponse> {
	return router(f)(this, dep1, dep2, dep3)
}
