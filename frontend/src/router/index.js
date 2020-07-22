import Vue from 'vue';
import VueRouter from 'vue-router';

import Home from '../views/Platform/Home.vue';
import Blog from '../views/Blog/Blog.vue';

Vue.use(VueRouter);

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
    },
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
