<template>
    <v-wait for="counsel loading">
        <template slot="waiting">
            <infinite-loading />
        </template>
        <div class="timeline">
            <CounselDetailComment
                name="question"
                :show-title="true"
                :title="post.title"
                :writer="post.writer"
                :content="post.content"
                :write-date="post.writeDate"
                :is-mine="post.mine"
                :is-author="true"
                :like-count="post.likeCount"
                :unlike-count="post.unlikeCount"

                :up-handler="upPost"
                :down-handler="downPost"
                :delete-handler="deletePost"
                :modify-handler="modifyPost"
                :report-handler="reportPost"
            />

            <template v-if="replies && replies.length != 0">
                <template v-for="reply in replies">
                    <CounselDetailComment
                        :key="reply.id"
                        name="reply"
                        :content="reply.content"
                        :writer="reply.writer"
                        :write-date="reply.writeDate"
                        :is-author="Math.random() > 0.6"
                        :like-count="reply.likeCount"
                        :unlike-count="reply.unlikeCount"
                    />
                </template>
            </template>

            <div class="comment">
                <editor 
                    ref="editorText" 
                    v-model="editorText" 
                    class="editor-left"
                    :options="editorOpts" 
                    initial-edit-type="wysiwyg" 
                    height="200px" 
                />
                <button
                    class="button"
                    @click.prevent="replyHandler"
                >
                    작성
                </button>
            </div>
        </div>
    </v-wait>
</template>

<script>
import CounselDetailComment from '@/components/counsel/CounselDetailComment.vue';
//import BaseEditor from '@/components/BaseEditor';
import CounselCommentEditor from '@/components/counsel/CounselCommentEditor';

export default {
    name: 'CounselDetail',
    components: {
        CounselDetailComment,
        //BaseEditor,
        // CounselCommentEditor,
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
    async created() {
        this.$wait.start('counsel loading');
        await this.$axios({
            url: '/counsel/post/post-no',
            method: 'get',
            headers: {
                'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                'nickname': sessionStorage.getItem('nickname'),
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
                    'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                    'nickname': sessionStorage.getItem('nickname'),
                },
                data: {
                    'content': content,
                    'postNo': this.no,
                    'secret': true,
                    'selected': true,
                    'writer': sessionStorage.getItem('nickname'),
                },
            })
                .then((r) => {
                    console.log(r);
                    this.$router.go();
                })
                .catch((e) => {
                    console.log(e.response);
                });
        },
        upPost(type) {
            this.$axios({
                url: '/counsel/post/like',
                method: 'post',
                headers: {
                    'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                    'nickname': sessionStorage.getItem('nickname'),
                },
                data: {
                    'postNo': this.no,
                    'type': type,
                },
            });
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