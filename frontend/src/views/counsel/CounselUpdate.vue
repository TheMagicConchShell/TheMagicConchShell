<template>
    <div>
        <h1> 고민 수정 </h1>
        <div 
            class="container" 
        >
            <div>
                <h2>
                    <label for="counseltitle"> 제목: </label>
                    <input 
                        id="counseltitle"
                        v-model="title"
                        type="text"
                        name="counseltitle"
                        placeholder="제목을 입력해주세요."
                        style="width:90%"
                    >
                </h2>
            </div>
            <div>
                <h2>
                    카테고리:
                    <input 
                        id="radio1"
                        v-model="category"
                        type="radio" 
                        name="category" 
                        value="1"  
                    >
                    <label for="radio1">연애</label>
                    <input 
                        id="radio2"
                        v-model="category"
                        type="radio" 
                        name="category" 
                        value="2"  
                    >                    
                    <label for="radio2">일상</label>
                    <input 
                        id="radio3"
                        v-model="category"
                        type="radio" 
                        name="category" 
                        value="3"
                    >                    
                    <label for="radio3">직장</label>
                    <input 
                        id="radio4"
                        v-model="category"
                        type="radio" 
                        name="category" 
                        value="4"  
                    >                    
                    <label for="radio4">결혼</label>
                    <input 
                        id="radio5"
                        v-model="category"
                        type="radio" 
                        name="category" 
                        value="5"  
                    >                    
                    <label for="radio5">문화</label>
                    <input 
                        id="radio6"
                        v-model="category"
                        type="radio" 
                        name="category" 
                        value="6"  
                    >                    
                    <label for="radio6">주식</label>
                    <input 
                        id="radio7"
                        v-model="category"
                        type="radio" 
                        name="category" 
                        value="7"  
                    >                    
                    <label for="radio7">치정</label>
                </h2>
            </div>
            <div>
                <h3> 
                    메인 화면에 게시 허용시 체크:
                    <input 
                        id="allow" 
                        v-model="allow"
                        style="width:5%" 
                        type="checkbox" 
                        name="allow" 
                    >
                    <label 
                        v-if="allow==true" 
                        for="allow"
                    >허용
                    </label>
                    <label 
                        v-else 
                        for="allow"
                    >허용하지 않음
                    </label>
                </h3>
            </div>
            <div>
                <h3>
                    익명으로 등록하기:
                    <input 
                        id="secret" 
                        v-model="secret"
                        style="width:5%" 
                        type="checkbox" 
                        name="secret" 
                    >
                    <label 
                        v-if="secret==true" 
                        for="secret"
                    >익명
                    </label>
                    <label 
                        v-else 
                        for="secret"
                    >닉네임 공개
                    </label>
                </h3>
            </div>
            <div>
                <editor 
                    v-if="editorText!=null"
                    ref="editorText" 
                    :initial-value="editorText"
                    :options="editorOpts" 
                    initial-edit-type="wysiwyg" 
                    height="500px" 
                />
                <button 
                    id="updatebtn" 
                    @click="updatecounsel"
                >
                    수정하기
                </button>
            </div>
        </div>
    </div>
</template>
<script>

import 'tui-color-picker/dist/tui-color-picker.css';
import colorSyntax from '@toast-ui/editor-plugin-color-syntax';

const storage = window.sessionStorage;

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
            allow:false,
            secret:false,
            category:0,
            msg:'',
            editorOpts:null
            
        };
    },
    mounted() {
        this.editorOpts = this.$store.getters.EDITOROPTIONS.options;
        this.$axios({
            method:"get",
            url:`/counsel/post/post-no?no=${this.no}`,

        }).then((res)=>{
            if(res.data.status==="S-200"){
                console.log(res.data.data);
                this.allow = res.data.data.post.allow;
                this.category = res.data.data.post.categoryId;
                this.secret = res.data.data.post.secret;
                this.title = res.data.data.post.title;
                this.editorText = res.data.data.post.content;
            }
        });
    },
    methods: {
        updatecounsel(){
            var contentHtml = this.$refs.editorText.invoke("getHtml");
            this.$axios({
                method:"put",
                url:"/counsel/post",
                headers:{
                    "jwt-auth-token" : storage.getItem("jwt-auth-token"),
                    "nickname": storage.getItem("nickname")

                },
                data:{
                    allow:this.allow,
                    categoryId:this.category,
                    content:contentHtml,
                    secret:this.secret,
                    title:this.title,
                    no: this.no
                }
            }).then((res)=>{
                if(res.data.status==="S-200"){
                    this.msg = "고민 수정에 성공햇습니다.";
                    this.$toast('고민 수정',this.msg);
                    this.$router.push(`/counsel/read/${this.no}`);
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

<style>
input[type=radio]{
    display: none;
    margin:10px;
}

input[type=radio] + label{
    display:inline-block; 
    margin:-2px; 
    padding: 8px 19px; 
    background-color: white; 
    border: 1px solid #ccc; 
    font-size: 13px !important; 
    width: 110px; 
    text-align: center; 
}

input[type=radio]:checked + label {  
    background-image: none; 
    background-color:black; 
    color:white; 
} 

h2{
    text-align: left;
}

h3{
    text-align: left;
}

#updatebtn{
    margin: 20px;
}
</style>