<template>
    <div calss="container">
        <b-card no-body>
            <b-tabs
                card
                fill
            >
                <b-tab
                    title="유저 목록"
                    active
                >
                    <b-card-text>현재 유저 목록</b-card-text>
                    <UserList />
                </b-tab>
                <b-tab title="카테고리 관리">
                    <CategoryManage />
                </b-tab>
                <b-tab title="선정 글 관리">
                    <CounselSelect />
                </b-tab>
                <b-tab title="Spot 관리">
                    <SpotManage />
                </b-tab>
            </b-tabs>
        </b-card>
    </div>
</template>

<script>
import UserList from '@/components/admin/UserList';
import CounselSelect from '@/components/admin/CounselSelect';
import CategoryManage from '@/components/admin/CategoryManage';
import SpotManage from '@/components/admin/SpotManage';

export default {
    components:{
        UserList,
        CounselSelect,
        CategoryManage,
        SpotManage,
    }
};
</script>

<style></style>
