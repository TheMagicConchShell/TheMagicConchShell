<template>
    <v-wait for="counsel loading">
        <template slot="waiting">
            <infinite-loading />
        </template>
        <div class="timeline">
            <CounselDetailComment
                name="question"
                :is-post="true"
                :title="post.title"
                :writer="post.writer"
                :content="post.content"
                :write-date="post.writeDate"
                :is-mine="post.mine"
                :is-author="true"
                :like-count="post.likeCount"
                :unlike-count="post.unlikeCount"
                :secret="post.secret"

                :like-handler="nickname ? likePost : dummy"
                :delete-handler="deletePost"
                :modify-handler="modifyPost"
                :report-handler="dummy"
            />

            <template v-if="replies && replies.length != 0">
                <template v-for="reply in replies">
                    <CounselDetailComment
                        :key="reply.id"
                        name="reply"
                        :reply-id="reply.id"
                        :content="reply.content"
                        :writer="reply.writer"
                        :write-date="reply.writeDate"
                        :is-author="reply.author"
                        :like-count="reply.likeCount"
                        :unlike-count="reply.unlikeCount"
                        :is-mine="reply.mine"
                        :secret="reply.secret"

                        :like-handler="nickname ? likeReply : dummy"
                        :delete-handler="dummy"
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
        jwtAuthToken: {
            get() {
                return this.$store.getters.jwtAuthToken;
            },
        },
    },
    async created() {
        this.$wait.start('counsel loading');
        await this.$axios({
            url: '/counsel/post/post-no',
            method: 'get',
            headers: {
                'jwt-auth-token': this.jwtAuthToken,
                'nickname': this.nickname,
            },
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
    methods: {
        replyHandler() {
            const content = this.$refs.editorText.invoke("getHtml");
            this.$axios({
                url: '/counsel/reply',
                method: 'post',
                headers: {
                    'jwt-auth-token': this.jwtAuthToken,
                    'nickname': this.nickname,
                },
                data: {
                    'content': content,
                    'postNo': this.no,
                    'secret': true,
                    'selected': true,
                    'writer': this.nickname,
                },
            })
                .then(() => {
                    this.$router.go();
                })
                .catch((e) => {
                    console.log(e.response);
                });
        },
        likePost(type) {
            this.$axios({
                url: '/counsel/post/like',
                method: 'post',
                headers: {
                    'jwt-auth-token': this.jwtAuthToken,
                    'nickname': this.nickname,
                },
                data: {
                    'postNo': this.no,
                    'type': type,
                },
            }).then(() => {
                this.$router.go();
            });
        },
        likeReply(type) {
            this.$axios({
                url: '/counsel/reply/like',
                method: 'post',
                headers: {
                    'jwt-auth-token': this.jwtAuthToken,
                    'nickname': this.nickname,
                },
                data: {
                    'replyId': this.no,
                    'type': type,
                },
            }).then(() => {
                this.$router.go();
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
            this.$axios({
                url: '/counsel/post',
                method: 'put',
                headers: {
                    'jwt-auth-token': this.jwtAuthToken,
                    'nickname': this.nickname,
                },
                data: {
                    'content': content,
                    "id": id,
                    "no": this.no,
                    'secret': secret,
                    "selected": true,
                    'writer': this.nickname,
                },
            }).then(() => {
                if (isPost) {
                    this.$router.push({path: '/counsel/read/' + this.no});
                } else {
                    this.$router.go();
                }
            }).catch((error) => {
                console.log(error);
            });
        },
        deletePost() {
            this.$axios({
                url: '/counsel/post',
                method: 'delete',
                headers: {
                    'jwt-auth-token': this.jwtAuthToken,
                    'nickname': this.nickname,
                },
                params: {
                    'no': this.no,
                },
            }).then(() => {
                this.$router.push({name: 'List'});
            });
        },
        dummy() {

        },
    }
};
</script>

<style scoped>
.timeline {
    margin-left: 40px;
    padding-left: 16px;
}
.button {
    float: right;
}
.comment {
    position: relative;
    width: 75%;
    left: -16px;
    min-height: 160px;
}
.editor-left {
    text-align: left;
}
</style>