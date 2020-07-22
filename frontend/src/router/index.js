import Vue from 'vue';
import VueRouter from 'vue-router';
import Home from '@/views/Home.vue';

import NoticeList from '@/components/NoticeList.vue';
import NoticeEditor from '@/components/NoticeEditor.vue';
import Support from '@/views/Support.vue';

Vue.use(VueRouter);

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
    },
    {
        path: '/about',
        name: 'About',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/About.vue'),
    },
    {
        name: 'support',
        path: '/support',
        component: Support,
        redirect: '/support/notice/1',
        children: [
            {
                name: 'notice-list-default',
                path: 'notice',
                redirect: '/support/notice/1',
            },
            {
                name: 'notice-write',
                path: 'notice/write',
                component: NoticeEditor,
            },
            {
                name: 'notice-list',
                path: 'notice/:page',
                props: true,
                component: NoticeList,
            },
        ],
    },
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes,
});

export default router;
