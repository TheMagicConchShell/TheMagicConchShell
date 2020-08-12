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
                    <ManageUserList />
                </b-tab>
                <b-tab title="카테고리 관리">
                    <ManageCategory />
                </b-tab>
                <b-tab title="선정 글 관리">
                    <ManageCounselSelect />
                </b-tab>
                <b-tab title="Spot 관리">
                    <SpotManage />
                </b-tab>
            </b-tabs>
        </b-card>
    </div>
</template>

<script>
import ManageUserList from '@/components/admin/ManageUserList';
import ManageCounselSelect from '@/components/admin/ManageCounselSelect';
import ManageCategory from '@/components/admin/ManageCategory';
import SpotManage from '@/components/admin/SpotManage';

export default {
    components:{
        ManageUserList,
        ManageCounselSelect,
        ManageCategory,
        SpotManage,
    }
};
</script>

<style></style>
