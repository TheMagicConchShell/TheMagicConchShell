import Vue from 'vue';
import VueRouter from 'vue-router';

import Home from '@/views/Platform/Home.vue';
import Open from '@/views/Platform/Open.vue';
import Blog from '@/views/Blog/Blog.vue';

import UserDetail from '@/components/user/UserDetail.vue';
import Signup from '@/components/user/Signup.vue';
import Login from '@/components/user/Login.vue';

import Support from '@/views/Support.vue';
import NoticeList from '@/components/NoticeList.vue';
import NoticeEditor from '@/components/NoticeEditor.vue';

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
    {
        path: '/user/detail',
        name: 'userdetail',
        component: UserDetail,
    },
    {
        path: '/user/signup',
        name: 'signup',
        component: Signup,
    },
    {
        path: '/user/login',
        name: 'login',
        component: Login,
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
