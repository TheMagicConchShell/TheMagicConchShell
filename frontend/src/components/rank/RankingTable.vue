<template>
    <div>
        <template v-if="tableType==='user'">
            <div>
                <UserTable />
            </div>
        </template>
        <template v-else-if="tableType==='post'">
            <div>
                <PostTable />
            </div>
        </template>
        <template v-else>
            <div>
                <ReplyTable />
            </div>
        </template>
    </div>
</template>

<script>
import UserTable from '@/components/rank/UserTable.vue';
import PostTable from '@/components/rank/PostTable.vue';
import ReplyTable from '@/components/rank/ReplyTable.vue';
export default {
    components:{
        UserTable,
        PostTable,
        ReplyTable
    },
    props:{
        tableType:{
            type: String,
            required: true,
        }
    }
};
</script>

<style>
</style>