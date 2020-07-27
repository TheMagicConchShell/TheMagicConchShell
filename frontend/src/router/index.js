import Vue from 'vue';
import VueRouter from 'vue-router';

import Home from '../views/Platform/Home.vue';
import Open from '../views/Platform/Open.vue';
import List from '../views/Board/List.vue';

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
    // 게시판
    {
        path: '/board',
        name: 'List',
        component: List,
    },
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes,
});

export default router;
