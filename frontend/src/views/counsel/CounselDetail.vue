<template>
    <v-wait for="counsel loading">
        <template slot="waiting">
            <infinite-loading />
        </template>
        
        <b-modal
            :id="`post-advertise-modal-${no}`"
            ref="modal"
            title="고민 광고"
        >
            <span>
                포인트를 소모해서 고민을 광고 하시겠습니까?
            </span>
            <template v-slot:modal-footer="{ hide }">
                <b-button
                    variant="outline-secondary"
                    size="sm"
                    class="float-right"
                    :disabled="$wait.is('counsel-chunk')"
                    @click="hide"
                >
                    취소
                </b-button>
                <b-button
                    variant="danger"
                    size="sm"
                    class="float-right"
                    :disabled="$wait.is('counsel-chunk')"
                    @click="advertiseHanlder"
                >
                    광고
                </b-button>
            </template>
        </b-modal>
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
                :is-post-owner="post.mine"
                :profile-img="post.profileImg"

                :like-handler="nickname ? likePost : dummy"
                :delete-handler="deletePost"
                :modify-handler="modifyPost"
                :report-handler="dummy"
                :advertise-caller="advertiseCaller"
            />

            <template v-if="selectedReply && selectedReply.id">
                <CounselDetailComment
                    :id="selectedReply.id"
                    :key="selectedReply.id"
                    name="selectedReply"
                    
                    :is-selected="true"
                    :content="selectedReply.content"
                    :writer="selectedReply.writer"
                    :write-date="selectedReply.writeDate"
                    :is-author="selectedReply.author"
                    :like-count="selectedReply.likeCount"
                    :unlike-count="selectedReply.unlikeCount"
                    :is-mine="selectedReply.mine"
                    :secret="selectedReply.secret"
                    :i-love-it="selectedReply.iloveIt"
                    :is-post-owner="post.mine"
                    :select-handler="selectHandler"
                    :profile-img="selectedReply.profileImg"

                    :like-handler="nickname ? likeReply : dummy"
                    :delete-handler="deleteReply"
                    :modify-handler="modifyReply"
                    :report-handler="dummy"
                />
            </template>
            <template v-if="replies && replies.length != 0">
                <template v-for="reply in replies">
                    <CounselDetailComment
                        :id="reply.id"
                        :key="reply.id"
                        name="reply"
                        
                        :hash="reply.sha256Name"
                        :is-selected="reply.selected"
                        :content="reply.content"
                        :writer="reply.writer"
                        :write-date="reply.writeDate"
                        :is-author="reply.author"
                        :like-count="reply.likeCount"
                        :unlike-count="reply.unlikeCount"
                        :is-mine="reply.mine"
                        :secret="reply.secret"
                        :i-love-it="reply.iloveIt"
                        :is-post-owner="post.mine"
                        :select-handler="selectHandler"
                        :profile-img="reply.profileImg"

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
                @submit="fetchPost"
            />
        </div>
    </v-wait>
</template>

<script>
import { mapGetters } from 'vuex';

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
        selectedReply: {},
        replies: [],
        editorText: '',
        editorOpts: {
            hideModeSwitch: true,
        },
    }),
    computed: {
        ...mapGetters(['nickname']),
    },
    async created() {
        await this.fetchPost();
    },
    methods: {
        async fetchPost() {
            this.$wait.start('counsel loading');
            // const currentPosition = window.scrollY || document.documentElement.scrollTop;

            const response = await this.$axios({
                url: '/counsel/post/post-no',
                method: 'get',
                params: {
                    no: this.no,
                },
            })
                .catch((error) => {
                    console.log(error);
                });

            if (response) {
                if (200 <= response.status && response.status < 300) {
                    // let formatDate = (date) => {
                    //     let d = new Date(date),
                    //         month = '' + (d.getMonth() + 1),
                    //         day = '' + d.getDate(),
                    //         year = d.getFullYear();

                    //     if (month.length < 2) 
                    //         month = '0' + month;
                    //     if (day.length < 2) 
                    //         day = '0' + day;

                    //     return [month, day].join('-');
                    // };

                    this.post = response.data.data.post;
                    // this.post.writeDate = formatDate(this.post.writeDate);
                    this.replies = response.data.data.replies;
                    // .map((e) => {
                    //     let n = e;
                    //     n.writeDate = formatDate(e.writeDate);
                    //     return n;
                    // });

                    // this.selectedReply = this.replies.find((e) => e.selected === true);
                    // this.replies = this.replies.filter((e) => e.selected !== true);
                }
            }

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
            })
                .then(() => {
                    this.fetchPost();
                })
                .catch((error) => {
                    const message = error.response.data.message;

                    this.$toast('좋아요를 누를 수 없습니다', message);
                })
                .finally(() => {
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
            })
                .then(() => {
                    this.fetchPost();
                })
                .catch((error) => {
                    const message = error.response.data.message;

                    this.$toast('좋아요를 누를 수 없습니다', message);
                })
                .finally(() => {
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
            })
                .then(() => {
                    this.fetchPost();
                })
                .catch((error) => {
                    const message = error.response.data.message;

                    this.$toast('답글 수정 실패', message);
                })
                .finally(() => {
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
            })
                .then(() => {
                    this.$router.push({ name: 'List' });
                })
                .catch((error) => {
                    const message = error.response.data.message;

                    this.$toast('고민 삭제 실패', message);
                })
                .finally(() => {
                    this.$wait.end('counsel-chunk');
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
            })
                .then(() => {
                    this.fetchPost();

                    this.$toast('답글 삭제 성공', '답글이 삭제되었습니다.');
                })
                .catch((error) => {
                    const message = error.response.data.message;

                    this.$toast('답글 삭제 실패', message);
                })
                .finally(() => {
                    this.$wait.end('counsel-chunk');
                });
        },
        selectHandler(replyId) {
            this.$wait.start('counsel-chunk');
            this.$axios({
                url: '/counsel/reply/select',
                method: 'put',
                params: {
                    'post_no': this.post.no,
                    'reply_id': replyId,
                },
            })
                .then((response) => {
                    this.fetchPost();
                    
                    this.$toast('답변 채택', '답변 채택에 성공했습니다.');
                })
                .catch((error) => {
                    const message = error.response.data.message;
                    this.$toast('답변 채택', message);
                })
                .finally(() => {
                    this.$wait.end('counsel-chunk');
                });
        },
        advertiseCaller() {
            this.$bvModal.show(`post-advertise-modal-${this.no}`);
        },
        advertiseHanlder(id) {
            this.$axios({
                url: '/spot',
                method: 'post',
                data: {
                    'owner': this.nickname,
                    'postNo': this.post.no,
                },
            })
                .then((response) => {
                    this.$toast('광고 등록에 성공했습니다.', '등록된 광고는 일정시간동안 상단영역에 노출됩니다.');
                })
                .catch((error) => {
                    const message = error.response.data.message;
                    this.$toast('광고 등록에 실패했습니다.', message);
                })
                .finally(() => {
                    this.$bvModal.hide(`post-advertise-modal-${this.no}`);
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