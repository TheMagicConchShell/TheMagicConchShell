<template>
    <div>
        <div 
            class="container" 
        >
            <div class="header">
                <span class="header-title">제목: </span>
                <input 
                    id="noticetitle"
                    v-model="title"
                    type="text"
                    placeholder="제목을 입력해주세요."
                    style="width:90%"
                >
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
            let submitData = null;
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
            const response = await this.$axios({
                url: this.submitUrl,
                method: this.submitMethod,
                headers: {
                    'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                    'nickname': sessionStorage.getItem('nickname'),
                },
                data:submitData
            })
                .catch((error) => { });

            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.$toast('공지사항', '공지사항이 작성되었습니다');
                    this.$router.push({
                        name: 'NoticeList',
                    });
                } else {
                }
            }
        },
    },
};
</script>

<style scoped>
.header {
    display: flex;
    flex-direction: row;
    align-items: center;
    font-size: 1.4rem;
    font-weight: bold;
}
.header-title {
    flex: 1;
    text-align: left;
}
</style>
