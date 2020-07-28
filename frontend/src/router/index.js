import Vue from 'vue';
import VueRouter from 'vue-router';

import Home from '@/views/Platform/Home.vue';
import Open from '@/views/Platform/Open.vue';
import Blog from '@/views/Blog/Blog.vue';

import UserDetail from '@/components/user/UserDetail.vue';
import Signup from '@/components/user/Signup.vue';
import Login from '@/components/user/Login.vue';
import FindPassword from '@/components/user/FindPassword.vue';
import EmailAuth from '@/components/user/EmailAuthentication.vue';

import Support from '@/views/support/Support.vue';
import NoticeView from '@/views/support/notice/NoticeView.vue';
import NoticeList from '@/views/support/notice/NoticeList.vue';
import NoticeRead from '@/views/support/notice/NoticeRead.vue';
import NoticeEditor from '@/views/support/notice/NoticeEditor.vue';
import NoticeUpdate from '@/views/support/notice/NoticeUpdate.vue';
import Error from '@/views/Error.vue';
import { readyException } from 'jquery';

import CounselRegist from '@/views/counsel/CounselRegist.vue';

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
        path: '/user/findpw',
        name: 'findpw',
        component: FindPassword,
    },
    {
        path: '/user/authentication',
        name: 'emailauth',
        component: EmailAuth,
    },
    {
        name: 'support',
        path: '/support',
        name: 'Support',
        component: Support,
        redirect: {
            name: 'NoticeList',
            query: {
                page: 1,
            },
        },
        children: [
            {
                path: 'notice',
                component: NoticeView,
                children: [
                    {
                        path: 'list',
                        name: 'NoticeList',
                        component: NoticeList,
                    },
                    {
                        path: 'write',
                        name: 'NoticeWrite',
                        component: NoticeEditor,
                    },
                    {
                        path: 'modify/:no',
                        name: 'NoticeUpdate',
                        props: ({params}) => ({no: Number.parseInt(params.no, 10) || 0}),
                        component: NoticeUpdate,
                    },
                    {
                        path: ':no',
                        name: 'NoticeRead',
                        props: ({params}) => ({no: Number.parseInt(params.no, 10) || 0}),
                        component: NoticeRead,
                    },
                ],
            },
        ],
    },
    {
        path:"/counselregist",
        name:"counselregist",
        component: CounselRegist
    },
    {
        path: "/error",
        name: "Error",
        component: Error,
    },
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes,
});

router.onError(() => {
    router.push({path: '/error'});
});

export default router;
