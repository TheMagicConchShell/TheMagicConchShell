<template>
    <v-wait for="counsel loading">
        <template slot="waiting">
            <infinite-loading />
        </template>
        <div class="timeline">
            <CounselDetailComment
                name="question"
                :is-post="true"
                :is-author="true"

                :title="post.title"
                :writer="post.writer"
                :content="post.content"
                :write-date="post.writeDate"
                :is-mine="post.mine"
                :like-count="post.likeCount"
                :unlike-count="post.unlikeCount"
                :secret="post.secret"
                :i-love-it="post.iloveIt"


                :like-handler="nickname ? likePost : dummy"
                :delete-handler="deletePost"
                :modify-handler="modifyPost"
                :report-handler="dummy"
            />

            <template v-if="replies && replies.length != 0">
                <template v-for="reply in replies">
                    <CounselDetailComment
                        :id="reply.id"
                        :key="reply.id"
                        name="reply"
                        
                        :content="reply.content"
                        :writer="reply.writer"
                        :write-date="reply.writeDate"
                        :is-author="reply.author"
                        :like-count="reply.likeCount"
                        :unlike-count="reply.unlikeCount"
                        :is-mine="reply.mine"
                        :secret="reply.secret"
                        :i-love-it="reply.iloveIt"

                        :like-handler="nickname ? likeReply : dummy"
                        :delete-handler="deleteReply"
                        :modify-handler="modifyReply"
                        :report-handler="dummy"
                    />
                </template>
            </template>

            <CounselCommentEditor
                v-if="nickname"
                :submit-url="'/counsel/reply'"
                :submit-method="'post'"
                :default-post-no="no"
                @submit="tempo"
            />
        </div>
    </v-wait>
</template>

<script>
import CounselDetailComment from '@/components/counsel/CounselDetailComment.vue';
import CounselCommentEditor from '@/components/counsel/CounselCommentEditor.vue';

export default {
    name: 'CounselDetail',
    components: {
        CounselDetailComment,
        CounselCommentEditor,
    },
    props: {
        no: {
            type: Number,
            required: true,
        }
    },
    data: () => ({
        post: {},
        replies: [],
        editorText: "",
        editorOpts: {
            hideModeSwitch: true,
        },
    }),
    computed: {
        nickname: {
            get() {
                return this.$store.getters.nickname;
            },
        },
    },
    async created() {
        await this.fetchPost();
    },
    methods: {
        tempo() {
            console.log("fetching...");
            this.fetchPost();
        },
        async fetchPost() {
            this.$wait.start('counsel loading');
            await this.$axios({
                url: '/counsel/post/post-no',
                method: 'get',
                params: {
                    no: this.no,
                },
            })
                .then((response) => {
                    console.log(response);
                    if (200 <= response.status && response.status < 300) {
                        let formatDate = (date) => {
                            let d = new Date(date),
                                month = '' + (d.getMonth() + 1),
                                day = '' + d.getDate(),
                                year = d.getFullYear();

                            if (month.length < 2) 
                                month = '0' + month;
                            if (day.length < 2) 
                                day = '0' + day;

                            return [month, day].join('-');
                        };

                        this.post = response.data.data.post;
                        this.post.writeDate = formatDate(this.post.writeDate);
                        this.replies = response.data.data.replies.map((e) => {
                            let n = e;
                            n.writeDate = formatDate(e.writeDate);
                            return n;
                        });
                    }
                })
                .catch((e) => {
                    console.log(e);
                });
            this.$wait.end('counsel loading');
        },
        likePost(type, id, iLoveIt, isDelete) {
            this.$wait.start('counsel-chunk');
            this.$axios({
                url: '/counsel/post/like',
                method: isDelete ? 'delete' : 'post',
                data: {
                    'postNo': this.no,
                    'type': type,
                },
            }).then(() => {
                this.fetchPost();
            }).finally(() => {
                this.$wait.end('counsel-chunk');
            });
        },
        likeReply(type, id, iLoveIt, isDelete) {
            this.$wait.start('counsel-chunk');
            this.$axios({
                url: '/counsel/reply/like',
                method: isDelete ? 'delete' : 'post',
                data: {
                    'replyId': id,
                    'type': type,
                },
            }).then(() => {
                this.fetchPost();
            }).finally(() => {
                this.$wait.end('counsel-chunk');
            });
        },
        modifyPost() {
            this.$router.push({
                name: 'CounselUpdate',
                params: {
                    no: this.no,
                },
            });
        },
        modifyReply(content, id, secret) {
            this.$wait.start('counsel-chunk');
            this.$axios({
                url: '/counsel/reply',
                method: 'put',
                data: {
                    'content': content,
                    "id": id,
                    "postNo": this.no,
                    'secret': secret,
                    "selected": true,
                    'writer': this.nickname,
                },
            }).then((res) => {
                if(res.data.status==="S-200"){
                    this.fetchPost();
                }
            }).catch((error) => {
                console.log(error);
            }).finally(() => {
                this.$wait.end('counsel-chunk');
            });
        },
        deletePost() {
            this.$wait.start('counsel-chunk');
            this.$axios({
                url: '/counsel/post',
                method: 'delete',
                params: {
                    'no': this.no,
                },
            }).then(() => {
                this.$router.push({name: 'List'});
            });
        },
        deleteReply(id) {
            this.$wait.start('counsel-chunk');
            this.$axios({
                url: '/counsel/reply',
                method: 'delete',
                params: {
                    'id': id,
                },
            }).then(() => {
                this.fetchPost();
            }).finally(() => {
                this.$wait.end('counsel-chunk');
            });
        },
        dummy() {

        },
    }
};
</script>

<style scoped>
.timeline {
    margin-left: 0px;
    padding-left: 0px;
}
.button {
    float: right;
}
.comment-area {
    position: relative;
    width: 75%;
    left: -16px;
    min-height: 160px;
}
.editor-left {
    text-align: left;
}
@media all and (min-width:768px) {
    .timeline {
        margin-left: 40px;
        padding-left: 16px;
    }
}
</style>