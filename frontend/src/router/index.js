import Vue from 'vue';
import VueRouter from 'vue-router';

import Home from '@/views/Platform/Home.vue';
import Open from '@/views/Platform/Open.vue';
import List from '@/views/Board/List.vue';
import Rank from '@/views/Board/Rank.vue';

import EmailAuthentication from '@/views/account/EmailAuthentication.vue';

import CounselView from '@/views/counsel/CounselView.vue';
import CounselDetail from '@/views/counsel/CounselDetail.vue';
import CounselRegist from '@/views/counsel/CounselRegist.vue';
import CounselSelectMain from '@/views/counsel/CounselSelectMain.vue';
import CounselUpdate from '@/views/counsel/CounselUpdate.vue';

import Support from '@/views/support/Support.vue';
import NoticeView from '@/views/support/notice/NoticeView.vue';
import NoticeList from '@/views/support/notice/NoticeList.vue';
import NoticeRead from '@/views/support/notice/NoticeRead.vue';
import NoticeEditor from '@/views/support/notice/NoticeEditor.vue';
import NoticeUpdate from '@/views/support/notice/NoticeUpdate.vue';
import Error from '@/views/Error.vue';

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
    {
        path: '/board',
        name: 'List',
        component: List,
    },
    {
        path: '/rank',
        name: 'Rank',
        component: Rank,
    },
    {
        path: '/user/authentication',
        name: 'EmailAuthentication',
        component: EmailAuthentication,
    },
    {
        path: '/counsel',
        name: 'Counsel',
        component: CounselView,
        children: [
            {
                path: 'read/:no',
                name: 'CounselDetail',
                props: ({params}) => ({no: Number.parseInt(params.no, 10) || 0}),
                component: CounselDetail,
            },
            {
                path:"regist",
                name:"counselregist",
                component: CounselRegist
            },
            {
                path: 'selectMain',
                name: 'CounselSelectMain',
                component: CounselSelectMain,
                query: {
                    page: 1,
                },
            },
            {
                path: 'update/:no',
                name: 'CounselUpdate',
                props: ({params}) => ({no: Number.parseInt(params.no, 10) || 0}),
                component: CounselUpdate
            }
        ],
    },
    {
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
                name: 'NoticeView',
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
