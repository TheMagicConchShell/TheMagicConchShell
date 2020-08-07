<template>
    <div class="comment-wrapper">
        <div class="comment">
            <div class="header-container">
                <div>
                    <span>답변 등록</span>
                </div>
            </div>
            <div class="comment-body-container">
                <div class="comment-body padding-right-view">
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
                        />
                        
                        <div class="comment-body-sidebar">
                            <button
                                class="btn btn-info comment-update-btn"
                                type="submit" 
                            >
                                등록
                            </button>
                            
                            <span class="comment-update-secret">
                                <label class="el-switch el-switch-sm">
                                    <input
                                        v-model="commentSecret"
                                        type="checkbox"
                                        name="switch"
                                        checked=""
                                    >
                                    <span
                                        class="el-switch-style"
                                    />
                                </label>
                                
                                <label
                                    for="commentupdatesecret"
                                    class="comment-update-secret-label"
                                >
                                    <span v-if="commentSecret">
                                        익명
                                    </span>
                                    <span v-else>
                                        공개
                                    </span>
                                </label>
                            </span>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import 'tui-color-picker/dist/tui-color-picker.css';
import colorSyntax from '@toast-ui/editor-plugin-color-syntax';
import { integer } from 'vee-validate/dist/rules';
import '@/assets/css/checkbox.css';

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
        commentSecret: true,
        editorOpts: null,
    }),
    computed: {
        nickname: {
            get() {
                return this.$store.getters.nickname;
            },
        },
    },
    created() {
        this.editorOpts = this.$store.getters.EDITOROPTIONS.options;
    },
    methods: {
        async submit() {
            this.content = this.$refs.commentEditorText.invoke("getHtml");

            const response = await this.$axios({
                url: this.submitUrl,
                method: this.submitMethod,
                data: {
                    content: this.content,
                    postNo: this.defaultPostNo,
                    secret: this.commentSecret,
                    selected: false,
                    writer: this.nickname,
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

<style lang="scss" scoped>
* {
    box-sizing: border-box;
    display: block;
}


.el-switch-sm {
    font-size: 60%;
}

label {
    margin-bottom: 0px;
}

.wide-only {
    display: none !important;
}

.comment-wrapper {
    position: relative;
    display: flex;
    width: 90%;
    left: 0px;
    margin-bottom: 22px;
    min-height: 160px;

    .comment {
        position: relative;
        border: 1px gray solid;
        flex: auto;
        border-radius: 6px;
        max-width: 100%;
    }
}

.comment-body-container {
    .comment-body-sidebar {
        margin-top: 12px;
        display: flex;
        flex-direction: column-reverse;
        align-items: center;

        .comment-update-btn{
            font-size: 100%;
            width: unset;
        }

        .comment-update-secret{
            display: flex;
            flex-direction: row;
            font-size: 100%;
            
            .comment-update-secret-checkbox {
                width: auto;
            }

            .comment-update-secret-label {
                margin-bottom: 0.1rem;
                margin-left: 2px;
            }
        }
    }
}

/**
 * Desktop CSS
 */
@media all and (min-width:768px) {
    .content {
        padding-right: 60px;
    }
    .wide-only {
        display: unset !important;
    }
    .mobile-only {
        display: none !important;
    }
    .comment-wrapper {
        width: 75%;
        left: -16px;
    }


    .el-switch-sm {
        font-size: 60%;

        .el-switch-style:before {
            top: 0.12em;
        }
    }
    
    .header-container {
        display: flex;
        flex-direction: column;

        align-items: center;
        top: 0px;
        max-width: 76px;
        left: -92px;
        position: absolute;
        z-index: 1;
        box-sizing: border-box;
        text-align: center;
        border-bottom: unset;
        font-size: 120%;

        .comment-update-secret{
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 100%;
            
            .comment-update-secret-label {
                margin-bottom: 0.1rem;
                margin-left: 2px;
            }
        }
    }
    
    .comment-body-container {
        .padding-right-view {
            padding-right: 60px;
        }
        .comment-body-sidebar {
            display: flex;
            flex-direction: column-reverse;

            .comment-update-btn{
                position: absolute;
                right: 4px;
                top: 12px;
                font-size: 100%;
                width: unset;
            }

            .comment-update-secret{
                display: unset;
                flex-direction: unset;
                position: absolute;
                right: 16px;
                top: 72px;
                font-size: 100%;
                
                .comment-update-secret-checkbox {
                    width: auto;
                }

                .comment-update-secret-label {
                    margin-bottom: 0.1rem;
                    margin-left: 2px;
                }
            }
        }
    }
}
</style>