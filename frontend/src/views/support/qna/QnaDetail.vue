<template>
    <v-wait for="counsel loading">
        <template slot="waiting">
            <infinite-loading />
        </template>
        <div class="timeline">
            <QnaDetailComment
                name="question"
                :is-question="true"

                :title="question.title"
                :writer="question.writer"
                :content="question.content"
                :write-date="question.writeDate"
                
                :delete-handler="deleteQuestion"
                :modify-handler="modifyQuestion"
                :report-handler="dummy"
            />

            <template v-if="answers && answers.length != 0">
                <template v-for="answer in answers">
                    <QnaDetailComment
                        :key="answer.no"
                        :no="answer.no"
                        name="answer"

                        :content="answer.content"
                        :writer="answer.writer"
                        :write-date="answer.writeDate"

                        :delete-handler="deleteAnswer"
                        :modify-handler="modifyAnswer"
                        :report-handler="dummy"
                    />
                </template>
            </template>

            <QnaCommentEditor
                v-if="nickname==='admin'||nickname===question.writer"
                :submit-url="'/qna/answer'"
                :submit-method="'post'"
                :default-qna-no="no"
                @submit="tempo"
            />
        </div>
    </v-wait>
</template>

<script>
import { mapGetters } from 'vuex';

import QnaDetailComment from '@/components/qna/QnaDetailComment.vue';
import QnaCommentEditor from '@/components/qna/QnaCommentEditor.vue';

export default {
    name: 'QnaDetail',
    components: {
        QnaDetailComment,
        QnaCommentEditor,
    },
    props: {
        no: {
            type: Number,
            required: true,
        }
    },
    data: () => ({
        question: {},
        answers: [],
        editorText: '',
        editorOpts: {
            hideModeSwitch: true,
        },
    }),
    computed: {
        ...mapGetters(['nickname']),
    },
    async created() {
        await this.fetchQuestion();
    },
    methods: {
        tempo() {
            this.fetchQuestion();
        },
        async fetchQuestion() {
            this.$wait.start('counsel loading');
            await this.$axios({
                url: '/qna/question/no',
                method: 'get',
                params: {
                    no: this.no,
                },
            })
                .then((response) => {
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

                        this.question = response.data.data.question;
                        this.question.writeDate = formatDate(this.question.writeDate);
                        this.answers = response.data.data.answers.map((e) => {
                            let n = e;
                            n.writeDate = formatDate(e.writeDate);
                            return n;
                        });
                    }
                })
                .catch((e) => {
                });
            this.$wait.end('counsel loading');
        },
        modifyQuestion() {
            this.$router.push({
                name: 'QnaUpdate',
                params: {
                    no: this.no,
                },
            });
        },
        modifyAnswer(content, no) {
            this.$wait.start('counsel-chunk');
            this.$axios({
                url: '/qna/answer',
                method: 'put',
                data: {
                    'content': content,
                    'no':no
                },
            }).then((res) => {
                if(res.data.status==="S-200"){
                    this.fetchQuestion();
                }
            }).catch((error) => {
            }).finally(() => {
                this.$wait.end('counsel-chunk');
            });
        },
        deleteQuestion() {
            this.$wait.start('counsel-chunk');
            this.$axios({
                url: '/qna/question',
                method: 'delete',
                params: {
                    'no': this.no,
                },
            }).then(() => {
                this.$router.push({name: 'QnaBoard'});
            });
        },
        deleteAnswer(no) {
            this.$wait.start('counsel-chunk');
            this.$axios({
                url: '/qna/answer',
                method: 'delete',
                params: {
                    'no': no,
                },
            }).then(() => {
                this.fetchQuestion();
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
    display: flex;
    flex-direction: column;
    align-items: center;
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