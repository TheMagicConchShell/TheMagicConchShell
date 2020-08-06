<template>
    <div>
        <h1> 고민 투고 </h1>
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
                            {{ item.name }}
                        </label>
                    </template>
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


export default {
    data () {
        return {
            editorText: '',
            title:'',
            allow:false,
            secret:false,
            category:0,
            msg:'',
            editorOpts: null
            
        };
    },
    computed: {
        nickname: {
            get() {
                return this.$store.getters.nickname;
            },
        },
        categories: {
            get() {
                return this.$store.getters.categories;
            },
        },
    },
    created() {
        this.$store.dispatch('fetchCategories');
        this.editorOpts = this.$store.getters.EDITOROPTIONS.options;
    },
    methods: {
        regist(){
            this.content = this.$refs.editorText.invoke("getHtml");
            this.$axios({
                method:"post",
                url:"/counsel/post",
                data:{
                    allow: this.allow,
                    categoryId: this.category,
                    content: this.content,
                    secret: this.secret,
                    title: this.title,
                    writer: this.nickname,
                }
            }).then((res)=>{
                if(res.data.status==="S-200"){
                    this.msg = "고민 투고에 성공했습니다. 고민 게시판으로 이동합니다.";
                    this.$toast('고민 투고',this.msg);
                    this.$router.push("/board");
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

#registbtn{
    margin: 20px;
}

.text-left {
    text-align: left;
}
</style>