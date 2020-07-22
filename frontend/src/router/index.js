import Vue from 'vue';
import VueRouter from 'vue-router';

import Home from '../views/Platform/Home.vue';
import Blog from '../views/Blog/Blog.vue';
// 유저
import UserDetail from '../components/user/UserDetail.vue';

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
    // 유저
    {
        path: '/user/detail',
        name: 'userdetail',
        component: UserDetail,
    },
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes,
});

export default router;
