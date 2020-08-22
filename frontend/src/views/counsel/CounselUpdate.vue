<template>
    <div>
        <h1>고민 투고</h1>
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
            <div class="d-flex-row">
                <span class="header">카테고리: </span>
                <div class="d-flex-row full">
                    <template v-for="item in categories">
                        <input
                            :id="`radio${item.id}`"
                            :key="`${item.id}-input`"
                            v-model="category"
                            type="radio" 
                            name="category" 
                            :value="item.id"
                        >
                        <label
                            :key="`${item.id}-label`"
                            :for="`radio${item.id}`"
                        >
                            {{ item.name[language] }}
                        </label>
                    </template>
                </div>
            </div>
            <div class="d-flex-row">
                <span class="header">메인 노출 허용: </span>
                <div class="d-flex-row full">
                    <label class="el-switch el-switch-sm">
                        <input
                            v-model="allow"
                            type="checkbox"
                            name="allow"
                            checked=""
                        >
                        <span
                            class="el-switch-style"
                        />
                    </label>
                    <label 
                        v-if="allow==true" 
                        for="allow"
                    >
                        허용
                    </label>
                    <label 
                        v-else 
                        for="allow"
                    >
                        허용하지 않음
                    </label>
                </div>
            </div>
            <div class="d-flex-row">
                <span class="header">익명으로 등록하기: </span>
                <div class="d-flex-row full">
                    <label class="el-switch el-switch-sm">
                        <input
                            v-model="secret"
                            type="checkbox"
                            name="secret"
                            checked=""
                        >
                        <span
                            class="el-switch-style"
                        />
                    </label>
                    <label
                        for="secret"
                    >
                        <span v-if="secret">
                            익명
                        </span>
                        <span v-else>
                            닉네임 공개
                        </span>
                    </label>
                </div>
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
                    @click="updateCounsel"
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
    computed: {
        ...mapGetters([
            'categories', 
            'language', 
            'nickname'
        ]),
    },
    mounted() {
        this.editorOpts = this.$store.getters.EDITOROPTIONS.options;
        this.$store.dispatch('fetchCategories');
        this.$axios({
            method:"get",
            url:`/counsel/post/post-no?no=${this.no}`,
        }).then((res) => {
            if(res.data.status === "S-200"){
                this.allow = res.data.data.post.allow;
                this.category = res.data.data.post.categoryId;
                this.secret = res.data.data.post.secret;
                this.title = res.data.data.post.title;
                this.editorText = res.data.data.post.content;
            }
        });
    },
    methods: {
        updateCounsel(){
            const contentHtml = this.$refs.editorText.invoke("getHtml");
            this.$axios({
                method:"put",
                url:"/counsel/post",
                data:{
                    allow: this.allow,
                    categoryId: this.category,
                    content: contentHtml,
                    secret: this.secret,
                    title: this.title,
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