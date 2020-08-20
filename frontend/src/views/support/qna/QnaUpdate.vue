<template>
    <div>
        <h1>문의 수정</h1>
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
                    v-if="editorText"
                    ref="editorText" 
                    :initial-value="editorText" 
                    class="text-left"
                    :options="editorOpts" 
                    initial-edit-type="wysiwyg" 
                    height="500px" 
                />
                <button 
                    id="registbtn" 
                    @click="updateQna"
                >
                    등록하기
                </button>
            </div>
        </div>
    </div>
</template>
<script>
import { mapGetters } from 'vuex';

export default {
    props: {
        no: {
            type: Number,
            required: true,
        }
    },
    data () {
        return {
            editorText: null,
            title:'',
            msg:'',
            editorOpts:null
            
        };
    },
    computed: {
        ...mapGetters(['nickname']),
    },
    mounted() {
        this.editorOpts = this.$store.getters.EDITOROPTIONS.options;
        this.$axios({
            method:"get",
            url:`/qna/question/no?no=${this.no}`,
        }).then((res) => {
            if(res.data.status === "S-200"){
                this.title = res.data.data.question.title;
                this.editorText = res.data.data.question.content;
            }
        });
    },
    methods: {
        updateQna(){
            var contentHtml = this.$refs.editorText.invoke("getHtml");
            this.$axios({
                method:"PUT",
                url:"/qna/question",
                data:{
                    content: contentHtml,
                    no: this.no,
                    title: this.title,
                }
            }).then((res)=>{
                if(res.data.status==="S-200"){
                    this.msg = "문의 수정에 성공햇습니다.";
                    this.$toast('문의 수정',this.msg);
                    //this.$router.push(`/qna/read/${this.no}`);
                }else{
                    this.msg = res.data.message;
                    this.$toast('안내',this.msg);
                }
            }).catch((e)=>{
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