<template>
    <div class="container">
        <ValidationObserver ref="observer">
            <div>
                <b-avatar
                    variant="info"
                    :src="imageUrl"
                    size="9em"
                    badge-variant="info" 
                />
                <b-button
                    id="profileBtn"
                    pill
                    type="button"
                    @click="onClickImageUpload"
                >
                    <i
                        class="fas fa-camera"
                    />
                </b-button>
                <input
                    id="profileImg"
                    ref="imageInput"
                    type="file"
                    accept="image/*"
                    hidden
                    @change="onChangeImages"
                >
            </div>
            <hr>

            <b-form-group
                label-cols-sm="2"
                label-cols-lg="2"
                label="이메일"
                label-for="email"
            >
                <b-input
                    id="email"
                    ref="email"
                    v-model="email"
                    type="text"
                    placeholder="ssafy@example.com"
                    readonly
                />
            </b-form-group>

            <b-form-group
                label-cols-sm="2"
                label-cols-lg="2"
                label="별명"
                label-for="nickname"
            >
                <ValidationProvider
                    v-slot="{ errors }"
                    class="input-wrap"
                    :rules="{required: true, max: 25}"
                >
                    <b-input
                        id="nickname"
                        ref="nickname"
                        v-model="nickname"
                        type="text"
                        placeholder="별명"
                    />
                    <div
                        v-if="errors[0]"
                        class="validation-fail-message"
                    >
                        {{ errors[0] }}
                    </div>
                </validationprovider>
            </b-form-group>

            <b-form-group
                label-cols-sm="2"
                label-cols-lg="2"
                label="비밀번호"
                label-for="password"
            >
                <ValidationProvider
                    v-slot="{ errors }"
                    class="input-wrap password-wrap"
                    :rules="{
                        required: true,
                        min: 8,
                        passwordRegex: true,
                        passwordConfirm: passwordConfirm }"
                >
                    <div id="passwordInput">
                        <b-input
                            id="password"
                            ref="password"
                            v-model="password"
                            :type="passwordType"
                            placeholder="영문/숫자 포함 8자 이상"
                        />
                        <i
                            :class="{ 'fa fa-eye fa-lg' : passwordType === 'password' ,
                                      'fa fa-eye-slash fa-lg' : passwordType === 'text' }"
                            @click="passwordType === 'text'?
                                passwordType = 'password': passwordType = 'text'"
                        />
                    </div>
                    <div
                        v-if="errors[0]"
                        class="validation-fail-message"
                    >
                        {{ errors[0] }}
                    </div>
                </validationprovider>
            </b-form-group>

            <b-form-group
                label-cols-sm="2"
                label-cols-lg="2"
                label="비밀번호 확인"
                label-for="passwordConfirm"
            >
                <ValidationProvider
                    v-slot="{ errors }"
                    class="input-wrap password-wrap"
                    vid="password-confirm"
                    rules="required"
                >
                    <b-input
                        id="passwordConfirm"
                        ref="passwordConfirm"
                        v-model="passwordConfirm"
                        :type="passwordType"
                        placeholder="비밀번호 확인"
                    />
                    <div
                        v-if="errors[0]"
                        class="validation-fail-message"
                    >
                        {{ errors[0] }}
                    </div>
                </validationprovider>
            </b-form-group>

            <div id="profileArea">
                <b-form-group
                    label-cols-sm="2"
                    label-cols-lg="2"
                    label="포인트"
                    label-for="point"
                >
                    <b-input
                        id="point"
                        ref="point"
                        v-model="point"
                        type="text"
                        readonly
                    />
                </b-form-group>

                <b-form-group
                    label-cols-sm="2"
                    label-cols-lg="2"
                    label="레벨"
                    label-for="level"
                >
                    <b-input
                        id="level"
                        ref="level"
                        v-model="level"
                        type="text"
                        readonly
                    />
                </b-form-group>
            </div>
            <div class="detail-button">
                <b-button @click="userUpdate">
                    정보 수정
                </b-button>
                &nbsp;
                <b-button
                    variant="danger"
                    @click="userDelete"
                >
                    회원 탈퇴
                </b-button>
            </div>
        </ValidationObserver>
        
        <hr>
        <div>
            <b-card no-body>
                <b-tabs
                    card
                    fill
                >
                    <b-tab
                        title="고민 투고 관리"
                        active
                    >
                        <b-card-text>Tab contents 1</b-card-text>
                    </b-tab>
                    <b-tab title="답변 관리">
                        <b-card-text>Tab contents 2</b-card-text>
                    </b-tab>
                    <b-tab title="포인트 사용 내역">
                        <PointHistory />
                    </b-tab>
                </b-tabs>
            </b-card>
        </div>

        <b-modal
            id="user-delete-check"
            title="탈퇴 완료"
            ok-only
            no-close-on-backdrop
            no-close-on-esc
            @ok="handleDeleteOk"
        >
            정상적으로 탈퇴 처리되었습니다.
        </b-modal>
    </div>
</template>

<script>
import PointHistory from '@/components/account/PointHistory.vue';

import {
    ValidationObserver,
    ValidationProvider,
    extend,
} from 'vee-validate';
import {
    min, max, required,
} from 'vee-validate/dist/rules';

extend('passwordConfirm', {
    params: ['target'],
    validate(value, { target }) {
        return value === target;
    },
    message: '비밀번호가 일치하지 않습니다.',
});

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

extend('passwordRegex', {
    validate(value) {
        return /^(?=.*?[A-Z])|(?=.*?[a-z])(?=.*?[0-9]).*$/.test(value);
    },
    message: '알파벳과 숫자를 각각 1개 이상 포함해야합니다.',
});

const storage = window.sessionStorage;

export default {
    components: {
        ValidationProvider,
        ValidationObserver,
        PointHistory
    },
    data: () => ({
        email: '',
        password: '',
        passwordConfirm: '',
        profileImg: '',
        point: '',
        level: '',
        passwordType: 'password',
        imageUrl: '',
        msg: '',
    }),
    computed: {
        nickname: {
            get() {
                return this.$store.getters.nickname;
            },
        },
    },
    created() {
        this.$axios({
            method: 'get',
            url: `/user/detail?nickname=${this.nickname}`,
        }).then((res) => {
            if (res.data.status === 'S-200') {
                this.email = res.data.data.email;
                this.profileImg = res.data.data.profileImg;
                this.point = res.data.data.point;
                this.level = res.data.data.level;
            }
        }).catch((error) => {
            console.log(error.response);
        });
    },
    methods: {
        async userUpdate() {
            const isValid = await this.$refs.observer.validate();

            if (!isValid) {
                this.msg = '필수 항목을 모두 입력해주세요.';
                this.$toast('안내', this.msg);
                return;
            }

            this.$axios({
                method: 'put',
                url: '/user/update',
                headers: {
                    nickname: this.nickname,
                },
                data: {
                    email: this.email,
                    nickname: this.nickname,
                    password: this.password,
                    profileImg: this.profileImg,
                },
            }).then((res) => {
                if (res.data.status === 'S-200') {
                    console.log(res);
                    storage.setItem('jwt-auth-token', res.headers['jwt-auth-token']);
                    storage.setItem('nickname', res.headers['nickname']);
                    this.msg = '수정 완료되었습니다.';
                    this.$toast('안내', this.msg);
                }
            }).catch((error) => {
                console.log(error.response);
            });
        },
        userDelete() {
            this.$axios({
                method: 'delete',
                url: `/user/delete?nickname=${this.nickname}`,
            }).then((res) => {
                storage.setItem('jwt-auth-token', '');
                storage.setItem('nickname', '');
                this.$bvModal.show('user-delete-check');
            }).catch((error) => {
                console.log(error.response);
            });
        },
        onClickImageUpload() {
            this.$refs.imageInput.click();
        },
        onChangeImages(e) {
            console.log(e.target.files);
            if(e.target.files){
                const file = e.target.files[0];
                this.imageUrl = URL.createObjectURL(file);
            }
        },
        handleDeleteOk() {
            this.$router.go();
        },
        movePointHistory() {
            this.$router.push({
                name: 'pointhistory'
            });
        }
    },
};
</script>

<style scoped>
.validation-fail-message {
  color: red;
}
#passwordInput {
    display: flex;
    align-items: center;
}
#passwordInput i{
    position:absolute;
    right:20px;
}
#profileBtn{
    position: relative;
    right: 30px;
    top: 50px;
}
#showPointHistoryBtn {
    margin-top: 10px;
}
.b-input-group {
    margin-bottom: 10px;
}
.detail-button {
    text-align: right;
}
</style>
