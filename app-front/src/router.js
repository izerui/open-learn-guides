import Vue from 'vue'
import Router from 'vue-router'
import Main from "./layout/Main";
import Login from "./layout/Login";

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            name: 'main',
            component: Main,
            children: [
                {
                    path: '/home',
                    name: 'home',
                    // route level code-splitting
                    // this generates a separate chunk (about.[hash].js) for this route
                    // which is lazy-loaded when the route is visited.
                    component: () => import('./views/Home.vue')
                },
                {
                    path: '/about',
                    name: 'about',
                    // route level code-splitting
                    // this generates a separate chunk (about.[hash].js) for this route
                    // which is lazy-loaded when the route is visited.
                    component: () => import('./views/About.vue')
                }
            ]
        },
        {
            path: '/login',
            name: 'login',
            component: Login,
        },
    ]
})
