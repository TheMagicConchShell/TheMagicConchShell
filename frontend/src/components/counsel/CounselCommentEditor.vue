<template>
    <div 
        id="commenteditor" 
        class="row"
    >
        <div class="col-md-2">
            <h2>
                답변 등록<br>
                <input 
                    id="commentsecret" 
                    v-model="commentsecret"
                    style="width:5%" 
                    type="checkbox" 
                    name="commentsecret" 
                >
                <label 
                    v-if="commentsecret==true" 
                    for="commentsecret"
                >익명
                </label>
                <label 
                    v-else 
                    for="commentsecret"
                >닉네임 공개
                </label>
            </h2>
        </div>
        <div class="col-md-10">
            <form
                ref="form"
                @submit.prevent="submit"
            >
                <editor 
                    ref="commentEditorText" 
                    v-model="content" 
                    class="text-left"
                    :options="editorOpts" 
                    initial-edit-type="wysiwyg" 
                    height="150px"
                    style="margin-top:20px;"
                />
                <button
                    id="commentsubmit"
                    type="submit" 
                >
                    등록
                </button>
            </form>
        </div>
    </div>
</template>

<script>
import 'tui-color-picker/dist/tui-color-picker.css';
import colorSyntax from '@toast-ui/editor-plugin-color-syntax';
import { integer } from 'vee-validate/dist/rules';

const storage = window.sessionStorage;

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
        defaultPostNo:{
            type: Number,
            required: true,
        }
    },
    data: () => ({
        content: '',
        commentsecret:true,
        editorOpts:null,
    }),
    created() {
        this.editorOpts = this.$store.getters.EDITOROPTIONS.options;
    },
    methods: {
        async submit() {
            this.content = this.$refs.commentEditorText.invoke("getHtml");

            const response = await this.$axios({
                url: this.submitUrl,
                method: this.submitMethod,
                headers: {
                    'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                    'nickname': sessionStorage.getItem('nickname'),
                },
                data: {
                    content:this.content,
                    postNo: this.defaultPostNo,
                    secret:this.commentsecret,
                    selected:false,
                    writer:sessionStorage.getItem("nickname")
                },
            })
                .catch((error) => { console.log(error.response); });

            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.$router.go();
                    this.$toast('답변', '답변이 작성되었습니다.');
                } else {
                    console.log('글 작성이 실패하였습니다');
                }
            }
        },
    },
};
</script>

<style>
#commenteditor {
    position: relative;
    display: flex;
    width: 85%;
    left: -70px;
    margin-bottom: 22px;
    min-height: 160px;
    margin-top:20px;
}

#commenteditor h2 {
    margin-top: 50px;
    text-align: left;
}
#commentsubmit {
    margin:20px;
}
.text-left {
    text-align: left;
}
</style>