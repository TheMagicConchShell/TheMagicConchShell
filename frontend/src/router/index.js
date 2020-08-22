import Vue from 'vue';
import VueRouter from 'vue-router';

import Home from '@/views/Platform/Home.vue';
import List from '@/views/Board/List.vue';
import Rank from '@/views/Board/Rank.vue';

import UserDetail from '@/views/account/UserDetail.vue';
import EmailAuthentication from '@/views/account/EmailAuthentication.vue';

import ManagePage from '@/views/admin/ManagePage.vue';

import CounselView from '@/views/counsel/CounselView.vue';
import CounselDetail from '@/views/counsel/CounselDetail.vue';
import CounselRegist from '@/views/counsel/CounselRegist.vue';
// import CounselSelect from '@/views/counsel/CounselSelect.vue';
import CounselUpdate from '@/views/counsel/CounselUpdate.vue';

import Support from '@/views/support/Support.vue';
import NoticeView from '@/views/support/notice/NoticeView.vue';
import NoticeList from '@/views/support/notice/NoticeList.vue';
import NoticeRead from '@/views/support/notice/NoticeRead.vue';
import NoticeEditor from '@/views/support/notice/NoticeEditor.vue';
import NoticeUpdate from '@/views/support/notice/NoticeUpdate.vue';

import QnaView from '@/views/support/qna/QnaView.vue';
import QnaBoard from '@/views/support/qna/QnaBoard.vue';
import QnaDetail from '@/views/support/qna/QnaDetail.vue';
import QnaRegist from '@/views/support/qna/QnaRegist.vue';
import QnaUpdate from '@/views/support/qna/QnaUpdate.vue';

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
        path: '/user/detail',
        name: 'userdetail',
        component: UserDetail,
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
            // {
            //     path: 'select',
            //     name: 'CounselSelect',
            //     component: CounselSelect,
            //     query: {
            //         page: 1,
            //     },
            // },
            {
                path: 'update/:no',
                name: 'CounselUpdate',
                props: ({params}) => ({no: Number.parseInt(params.no, 10) || 0}),
                component: CounselUpdate,
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
            {
                path:'qna',
                name:'QnaView',
                component:QnaView,
                children:[
                    {
                        path:'board',
                        name:'QnaBoard',
                        component:QnaBoard
                    },
                    {
                        path: 'read/:no',
                        name: 'QnaDetail',
                        props: ({params}) => ({no: Number.parseInt(params.no, 10) || 0}),
                        component: QnaDetail,
                    },
                    {
                        path:"regist",
                        name:"QnaRegist",
                        component: QnaRegist
                    },
                    {
                        path: 'update/:no',
                        name: 'QnaUpdate',
                        props: ({params}) => ({no: Number.parseInt(params.no, 10) || 0}),
                        component: QnaUpdate,
                    }
                ]
            }
        ],

    },
    {
        path: '/manager',
        name: 'managepage',
        component: ManagePage,
    },
    {
        path: "/error",
        name: "Error",
        component: Error,
    },
    {
        path: '*',
        name: 'NotFound',
        component: Error,
        props: {
            error: {
                CODE: 404,
                MESSAGE: '페이지를 찾을 수 없습니다.'
            },
        },
    },
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes,
    scrollBehavior () {
        return { x: 0, y: 0 };
    }
});

router.onError(() => {
    router.push({name: 'Error'});
});

export default router;
