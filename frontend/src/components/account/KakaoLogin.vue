<template>
    <div>
        <a 
            id="kakao-login-btn"
            ref="kakaoLogin"
        />
        <b-modal
            id="snsNickname"
            title="닉네임 등록"
            hide-footer
        >
            <h3>닉네임 등록하세요</h3>
            <div class="row">
                <div class="col-7">
                    <ValidationObserver ref="observer">
                        <!-- 닉네임 -->
                        <div>
                            <ValidationProvider
                                v-slot="{ errors }"
                                class="input-wrap"
                                :rules="{required: true, max: 25}"
                            >
                                <input
                                    id="nicknameR"
                                    ref="nicknameR"
                                    v-model="nicknameR"
                                    type="text"
                                    placeholder="별명"
                                >
                                <div
                                    v-if="errors[0]"
                                    class="validation-fail-message"
                                >
                                    {{ errors[0] }}
                                </div>
                            </validationprovider>
                        </div>
                    </ValidationObserver>
                </div>
            </div>
            <div class="float-right">
                <b-button
                    @click.prevent="snsRegist"
                >
                    등록하기
                </b-button>
            </div>
        </b-modal>
    </div>
</template>

<script>
import axios from 'axios';
import {
    ValidationObserver,
    ValidationProvider,
    extend,
} from 'vee-validate';
import {
    min, max, required,
} from 'vee-validate/dist/rules';

extend('required', {
    ...required,
    message: '반드시 입력해야하는 항목입니다.',
});

extend('min', {
    ...min,
    message: '{length}글자 이상으로 입력해주세요.',
});

extend('max', {
    ...max,
    message: '{length}글자 이하로 입력해주세요.',
});

export default {
    components: {
        ValidationProvider,
        ValidationObserver,
    },
    data: () => ({
        nicknameR: '',
        userId:'',
        userEmail:''
    }),
    async mounted() {
        await Kakao.Auth.createLoginButton({
            container: this.$refs.kakaoLogin,
            success: (authObj)=> {
                //console.log(authObj);
                Kakao.API.request({
                    url: '/v2/user/me',
                    success: (res) => {
                        //console.log(res);
                        //console.log(authObj);
                        this.userId = res.id; 
                        if(res.kakao_account.has_email==true){
                            this.userEmail = res.kakao_account.email;
                        }
                        this.$axios({
                            method:"post",
                            url:"/sns/login",
                            params:{
                                id:res.id
                            }
                        }).then((response)=>{
                            Kakao.Auth.setAccessToken(authObj.access_token);
                            const jwtAuthToken = response.headers['jwt-auth-token'];
                            const nickname = response.headers['nickname'];
                            this.$store.commit('LOGIN_SUCCESS', {jwtAuthToken,nickname});
                            console.log("성공");
                        }).catch(e=>{
                            this.$bvModal.show("snsNickname");
                            console.log(e);
                        });
                    },
                    fail: function(error) {
                        alert(
                            'login success, but failed to request user information: ' +
                            JSON.stringify(error)
                        );
                    },
                });
            },
            fail: function(err){
                alert(JSON.stringify(err));
            }
        });
    },
    updated() {
        console.log(check);
    },
    methods: {
        snsRegist(){
            var data = null;
            if(this.userEmail!=null){
                data = {
                    id:this.userId,
                    nickname:this.nicknameR,
                    email:this.userEmail
                };
            }else{
                data = {
                    id:this.userId,
                    nickname:this.nicknameR,
                };
            }
            this.$axios({
                method:"post",
                url:"/sns/post",
                params:data
            }).then((res)=>{
                this.msg = res.data.message+"다시 한번 로그인 버튼을 눌러주세요.";
                this.$toast("안내", this.msg);
                this.$bvModal.hide("snsNickname");
            }).catch(e=>{
                this.msg = e.data.message;
                this.$toast("안내", this.msg);
                this.$bvModal.hide("snsNickname");
            });
        }
    },
    
};
</script>

<style scoped>
#button {
    height: 100%;
    border: 0;
}
.validation-fail-message {
  color: red;
}
</style>