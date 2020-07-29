<template>
    <v-wait for="counsel loading">
        <template slot="waiting">
            <infinite-loading />
        </template>
        <div class="timeline">
            <CounselDetailComment
                name="question"
                :writer="'익명의 긴 이름을 가진 소라고동'"
                :write-date="'20.04.04'"
                :is-mine="true"
                :is-author="true"
            />

            <template v-for="i in 10">
                <CounselDetailComment
                    :key="i"
                    name="reply"
                    :writer="'익명의 소라고동'"
                    :write-date="'20.04.04'"
                    :is-author="Math.random() > 0.6"
                />
            </template>
            <BaseEditor
                :submit-url="'/conunsel/read/' + no"
                :submit-method="'post'"
            />
        </div>
    </v-wait>
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
    async created() {
        this.$wait.start('counsel loading');
        this.$axios({
            url: '/conusel/post/post-no',
            method: 'get',
            headers: {
                'jwt-auth-token': sessionStorage.getItem['jwt-auth-token'],
                'nickname': sessionStorage.getItem['nickname'],
            },
            params: {
                no: this.no,
            },
        }).then((response) => {
            console.log(response);
        }).catch(() => {
            
        });
        this.$wait.end('counsel loading');
    },
};
</script>

<style scoped>
.timeline {
    margin-left: 40px;
    padding-left: 16px;
}
</style>