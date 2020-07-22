import Vue from 'vue';
import VueRouter from 'vue-router';

import Home from '../views/Platform/Home.vue';
import Open from '../views/Platform/Open.vue';
import Blog from '../views/Blog/Blog.vue';

Vue.use(VueRouter);

const routes = [
    // 플랫폼
    {
        path: '/',
        name: 'Home',
        component: Home,
    },
    {
        path: '/open',
        name: 'Open',
        component: Open,
    },
    // 블로그
    {
        path: '/blog',
        name: 'Blog',
        component: Blog,
    },
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes,
});

export default router;
