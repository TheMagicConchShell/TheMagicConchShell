<template>
    <div>
        <div 
            class="container" 
        >
            <div>
                <h2>
                    <label for="noticetitle"> 제목: </label>
                    <input 
                        id="noticetitle"
                        v-model="title"
                        type="text"
                        name="noticetitle"
                        placeholder="제목을 입력해주세요."
                        style="width:90%"
                    >
                </h2>
            </div>
            <div>
                <editor 
                    v-if="content!=null"
                    ref="editorText" 
                    class="text-left"
                    :initial-value="content"
                    :options="editorOpts" 
                    initial-edit-type="wysiwyg" 
                    height="500px" 
                />
                <button 
                    id="submitbtn" 
                    @click="submit"
                >
                    작성
                </button>
            </div>
        </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
    props: {
        submitUrl: {
            type: String,
            required: true,
        },
        submitMethod: {
            type: String,
            required: true,
        },
        defaultNid: {
            type: Number,
            required: false,
            default: 0,
        },
        defaultWriter: {
            type: String,
            required: false,
            default: '',
        },
        defaultTitle: {
            type: String,
            required: false,
            default: '',
        },
        defaultContent: {
            type: String,
            required: false,
            default: '',
        },
    },
    data: () => ({
        nid: '',
        title: '',
        writer: '',
        content: null,
        editorOpts:null
    }),
    computed: {
        ...mapGetters(['nickname']),
    },
    watch: {
        defaultTitle() {
            this.nid = this.defaultNid;
            this.writer = this.defaultWriter;
            this.title = this.defaultTitle;
            this.content = this.defaultContent;
        },
    },
    mounted() {
        this.nid = this.defaultNid;
        this.writer = this.defaultWriter;
        this.title = this.defaultTitle;
        this.content = this.defaultContent;
        this.editorOpts = this.$store.getters.EDITOROPTIONS.options;
    },
    methods: {
        async submit() {
            var submitData = null;
            if(this.submitMethod==='post'){
                submitData = {
                    title: this.title,
                    writer: sessionStorage.getItem('nickname'),
                    content: this.$refs.editorText.invoke("getHtml"),
                };
            }else if(this.submitMethod==='put'){
                submitData = {
                    nid: this.nid,
                    title: this.title,
                    writer: sessionStorage.getItem('nickname'),
                    content: this.$refs.editorText.invoke("getHtml"),
                    writeDate:"2020-08-03T05:57:45"
                };
            }
            console.log(submitData);
            const response = await this.$axios({
                url: this.submitUrl,
                method: this.submitMethod,
                headers: {
                    'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                    'nickname': sessionStorage.getItem('nickname'),
                },
                data:submitData
            })
                .catch((error) => { console.log(error.response); });

            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.$toast('공지사항', '공지사항이 작성되었습니다');
                    this.$router.push({
                        name: 'NoticeList',
                    });
                } else {
                    console.log('글 작성이 실패하였습니다');
                }
            }
        },
    },
};
</script>

<style>

</style>
