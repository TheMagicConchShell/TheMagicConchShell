<template>
    <div>
        <h1>문의하기</h1>
        <div 
            class="container d-flex-column text-left"
        >
            <div class="d-flex-row">
                <span class="header">제목: </span>
                <input
                    v-model="title"
                    type="text"
                    placeholder="제목을 입력해주세요."
                >
            </div>
            <div>
                <editor 
                    v-if="editorOpts"
                    ref="editorText" 
                    v-model="editorText" 
                    class="text-left"
                    :options="editorOpts" 
                    initial-edit-type="wysiwyg" 
                    height="500px" 
                />
                <button 
                    id="registbtn" 
                    @click="regist"
                >
                    등록하기
                </button>
            </div>
        </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';

import 'tui-color-picker/dist/tui-color-picker.css';
import colorSyntax from '@toast-ui/editor-plugin-color-syntax';

export default {
    props: {
        no: {
            type: Number,
            default: 0,
        }
    },
    data: () => ({
        editorText: '',
        title: '',
        msg: '',
        editorOpts: null
    }),
    computed: {
        ...mapGetters(['nickname']),
    },
    created() {
        this.editorOpts = this.$store.getters.EDITOROPTIONS.options;
    },
    methods: {
        regist(){
            this.content = this.$refs.editorText.invoke("getHtml");
            this.$axios({
                method:"post",
                url:"/qna/question",
                data:{
                    content: this.content,
                    title: this.title,
                    writer: this.nickname,
                }
            }).then((res)=>{
                if(res.data.status==="S-200"){
                    this.msg = "문의에 성공했습니다. 문의 게시판으로 이동합니다.";
                    this.$toast('문의',this.msg);
                    this.$router.push("/qna/board");
                }else{
                    this.msg = res.data.message;
                    this.$toast('안내',this.msg);
                }
            }).catch((e)=>{
                console.log(e.response);
            });
        }
    },
};

</script>

<style scoped>
.el-switch {
    margin-right: 6px;
}
label {
    margin-top: .5rem;
    margin-bottom: .5rem;
}
.header {
    width: 160px;
}
.full {
    width: 100%;
}
.d-flex-column {
    display: flex;
    flex-direction: column;
}
.d-flex-row {
    display: flex;
    flex-direction: row;
    align-items: center;
}

input[type=text] {
    width: 100%;
}

input[type=radio] {
    display: none;
    margin:10px;
}

input[type=radio] + label {
    flex: 1;
    margin:-2px;
    padding: 8px 19px;
    background-color: white;
    border: 1px solid #ccc;
    text-align: center; 
}

input[type=radio]:checked + label {  
    background-image: none;
    background-color:black;
    color:white;
} 

#registbtn {
    margin: 20px;
}

.text-left {
    text-align: left;
}
</style>