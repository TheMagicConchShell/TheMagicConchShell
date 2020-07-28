<template>
    <div class="timeline">
        <CounselDetailComment
            name="question"
            :writer="'익명의 긴 이름을 가진 소라고동'"
            :write-date="'20.04.04'"
            :is-writer="true"
        />

        <template v-for="i in 10">
            <CounselDetailComment
                :key="i"
                name="reply"
                :writer="'익명의 소라고동'"
                :write-date="'20.04.04'"
            />
        </template>
        <BaseEditor
            :submit-url="'/conunsel/read/' + no"
            :submit-method="'post'"
        />
    </div>
</template>

<script>
import CounselDetailComment from '@/components/counsel/CounselDetailComment.vue';
import BaseEditor from '@/components/BaseEditor';

export default {
    name: 'CounselDetail',
    components: {
        CounselDetailComment,
        BaseEditor,
    },
    props: {
        no: {
            type: Number,
            required: true,
        }
    },
    created() {
        this.$axios({
            url: '/counsel/',
            method: 'get'
        });
        
    },
};
</script>

<style scoped>
.timeline {
    margin-left: 40px;
    padding-left: 16px;
}
</style>