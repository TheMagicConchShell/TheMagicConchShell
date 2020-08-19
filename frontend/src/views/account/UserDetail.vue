<template>
    <div class="container">
        <ValidationObserver ref="observer">
            <div>
                <b-avatar
                    button
                    class="profile-avatar"
                    variant="info"
                    :src="profileImgUrl"
                    size="9em"
                    badge-variant="info"
                    @click="onClickImageUpload"
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
                    id="profileImgFile"
                    ref="imageInput"
                    type="file"
                    accept="image/*"
                    hidden
                    @change="onChangeImages"
                >
            </div>
            <hr>

            <b-form-group
                v-if="!isSocialAccount"
                label-cols-sm="2"
                label-cols-lg="2"
                :label="$t('account.info.email')"
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
                :label="$t('account.info.nickname')"
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
                        v-model="nicknameInput"
                        type="text"
                        :placeholder="$t('account.info.nickname')"
                        :disabled="!editable"
                    />
                    <div
                        v-if="errors[0]"
                        class="validation-fail-message"
                    >
                        {{ errors[0] }}
                    </div>
                </validationprovider>
            </b-form-group>

            <div v-show="editable">
                <b-form-group
                    v-if="!isSocialAccount"
                    label-cols-sm="2"
                    label-cols-lg="2"
                    :label="$t('account.info.password')"
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
                                :placeholder="$t('account.info.holder.password')"
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
                    v-if="!isSocialAccount"
                    label-cols-sm="2"
                    label-cols-lg="2"
                    :label="$t('account.info.check')"
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
                            :placeholder="$t('account.info.check')"
                        />
                        <div
                            v-if="errors[0]"
                            class="validation-fail-message"
                        >
                            {{ errors[0] }}
                        </div>
                    </validationprovider>
                </b-form-group>
            </div>

            <div>
                <b-form-group
                    label-cols-sm="2"
                    label-cols-lg="2"
                    :label="$t('account.info.point.name')"
                    label-for="point"
                >
                    <b-input
                        id="point"
                        ref="point"
                        v-model="point"
                        type="text"
                        readonly
                        disabled
                    />
                </b-form-group>

                <b-form-group
                    label-cols-sm="2"
                    label-cols-lg="2"
                    :label="$t('account.info.level')"
                    label-for="level"
                >
                    <div class="d-flex">
                        <b-input
                            id="level"
                            ref="level"
                            v-model="level"
                            type="number"
                            readonly
                            disabled
                        />
                        <b-button             
                            v-b-modal.ask-level-up
                            variant="outline-primary"
                            class="level-up-button"
                        >
                            LEVEL <i class="fas fa-level-up-alt" />
                            <AskLevelup 
                                :level="level"
                                :levelup-require-point="levelupRequirePoint"
                                @levelup="levelup"
                            />
                            <LevelupCelebrate />
                        </b-button>
                    </div>
                </b-form-group>
            </div>

            <div
                v-show="editable"
                class="detail-button"
            >
                <b-button @click="userUpdate">
                    {{ $t('account.info.update') }}
                </b-button>
                &nbsp;
                <b-button
                    variant="danger"
                    @click="userDelete"
                >
                    {{ $t('account.info.delete') }}
                </b-button>
            </div>
            <b-button
                pill
                variant="outline-primary"
                type="button"
                @click="editable = !editable"
            >
                <i
                    class="fas fa-pencil-alt"
                />
            </b-button>
        </ValidationObserver>

        <hr>
        <div>
            <b-card no-body>
                <b-tabs
                    card
                    fill
                >
                    <b-tab
                        :title="$t('account.titles.post')"
                        active
                    >
                        <MyPost />
                    </b-tab>
                    <b-tab
                        :title="$t('account.titles.reply')"
                    >
                        <MyReply />
                    </b-tab>
                    <b-tab
                        :title="$t('account.titles.point')"
                    >
                        <PointHistory
                            ref="pointHistory"
                        />
                    </b-tab>
                </b-tabs>
            </b-card>
        </div>

        <b-modal
            id="user-delete-check"
            :title="$t('account.delete.done')"
            ok-only
            no-close-on-backdrop
            no-close-on-esc
            @ok="handleDeleteOk"
        >
            {{ $t('account.delete.message') }}
        </b-modal>
    </div>
</template>

<script>
import PointHistory from '@/components/account/PointHistory.vue';
import MyPost from '@/components/account/MyPost.vue';
import MyReply from '@/components/account/MyReply.vue';
import AskLevelup from '@/components/account/AskLevelup.vue';
import LevelupCelebrate from '@/components/account/LevelupCelebrate.vue';

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

import { mapGetters } from 'vuex';

export default {
    components: {
        ValidationProvider,
        ValidationObserver,
        PointHistory,
        MyPost,
        MyReply,
        AskLevelup,
        LevelupCelebrate,
    },
    data: () => ({
        nicknameInput: '',
        email: '',
        password: '',
        passwordConfirm: '',
        profileImgFile: '',
        profileImgUrl: '',
        point: '',
        level: 0,
        passwordType: 'password',
        msg: '',
        isSocialAccount: false,
        editable: false,
        levelupRequirePoint: 0,
    }),
    computed: {
        ...mapGetters(['nickname']),
    },
    created() {
        this.nicknameInput = this.nickname;
        this.userFetch();
    },
    methods: {
        async userFetch() {
            await this.$store.dispatch('fetchUser', {
                nickname: this.nicknameInput
            })
                .then((res) => {
                    console.log(res);
                    if (res.data.status === 'S-200') {
                        this.email = res.data.data.email;
                        this.profileImgUrl = res.data.data.profileImg;
                        this.point = res.data.data.point;
                        this.level = res.data.data.level;
                        this.isSocialAccount = res.data.data.socialAccount;
                        this.levelupRequirePoint = (this.level + 1) * 100;
                    }
                })
                .catch((error) => {
                    console.log(error.response);
                });
        },
        async userUpdate() {
            const isValid = await this.$refs.observer.validate();

            if (!isValid) {
                this.msg = '필수 항목을 모두 입력해주세요.';
                this.$toast('안내', this.msg);
                return;
            }

            const formData = new FormData();
            formData.append('profileImg', this.profileImgFile);
            formData.append('email', this.email);
            formData.append('nickname', this.nicknameInput);
            formData.append('password', this.password);

            this.$store.dispatch('updateUser', formData)
                .then((res) => {
                    if (res.data.status === 'S-200') {
                        console.log(res);
                        
                        this.msg = '수정 완료되었습니다.';
                        this.$toast('안내', this.msg);
                    }
                })
                .catch((error) => {
                    console.log(error.response);
                });
        },
        userDelete() {
            this.$store.dispatch('deleteUser', { 
                nickname: this.nickname,
            })
                .then((res) => {
                    this.$bvModal.show('user-delete-check');
                    Kakao.API.request({
                        url: '/v1/user/unlink',
                        success: (response)=>{
                            console.log(response);
                            this.$router.push({ name: 'Home'});
                        },
                        fail: function(error) {
                            console.log(error);
                        },
                    });

                })
                .catch((error) => {
                    console.log(error.response);
                });
        },
        onClickImageUpload() {
            this.$refs.imageInput.click();
        },
        onChangeImages(e) {
            if(e.target.files && e.target.files[0]){
                let isValidSize = this.checkImageSize(e.target);
                if(!isValidSize) {
                    this.msg = '프로필 이미지는 5mb 이하로 등록 가능합니다.';
                    this.$toast('안내', this.msg);
                    return;
                }

                this.profileImgFile = e.target.files[0];
                this.profileImgUrl = URL.createObjectURL(this.profileImgFile);
            }
        },
        checkImageSize(file) {
            const maxSize  = 5 * 1024 * 1024;
            let fileSize = 0;

            const browser = navigator.appName;
        
            if (browser == "Microsoft Internet Explorer") {
                const oas = new ActiveXObject("Scripting.FileSystemObject");
                fileSize = oas.getFile( file.value ).size;
            }
            else {
                fileSize = file.files[0].size;
            }
            
            return fileSize <= maxSize;
        },
        handleDeleteOk() {
            this.$router.push('/');
        },
        levelup() {
            this.$axios({
                url: `/point/level-up`,
                method: "put",
            }).then(() => {
                this.$bvModal.hide('ask-level-up');
                this.$bvModal.show('level-up-celebrate');
                this.userFetch();
                this.$refs.pointHistory.fetchPointHistory(1);
            }).catch((error) => {
                console.log(error.response);
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
    right: 10px;
    top: 50px;
}
#showPointHistoryBtn {
    margin-top: 10px;
}
.b-input-group {
    margin-bottom: 10px;
}
.detail-button {
    text-align: center;
    margin-bottom: 1rem;
}
.profile-avatar {
    left: 24px;
}
.level-up-button {
    flex: 1 1 100px;
    margin-top: 10px;
    margin-bottom: 10px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
</style>
