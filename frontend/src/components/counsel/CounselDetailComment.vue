<template>
    <div class="comment-wrapper">
        <div class="comment text-left">
            <div class="title-container">
                <div class="buttons">
                    <div
                        v-if="isAuthor"
                        class="button-author wide-only"
                    >
                        <span>AUTHOR</span>
                    </div>
                    <div
                        v-if="isAuthor"
                        class="mobile-only"
                    >
                        <svg-author />
                    </div>

                    <template v-if="isMine">
                        <div 
                            v-if="nickname && !isPost"
                            class="button-item"
                            @click="changeUpdate"
                        >
                            <svg-pencil />
                        </div>
                        <div
                            v-if="nickname && isPost"
                            class="button-item"
                            @click="modifyHandler"
                        >
                            <svg-pencil />
                        </div>
                        <div
                            v-if="nickname"
                            class="button-item"
                            @click="deleteHandler(id)"
                        >
                            <svg-delete />
                        </div>
                    </template>
                    <template v-else>
                        <div
                            class="button-item"
                            @click="reportHandler"
                        >
                            <svg-exclamation />
                        </div>
                    </template>
                </div>
                <div
                    v-if="isPost"
                    class="title-text"
                >
                    {{ title }}
                </div>
            </div>

            <div class="avatar-container">
                <img
                    class="avatar"
                    src="https://img.kbs.co.kr/kbs/620/nsimg.kbs.co.kr/data/news/2010/01/08/2024781_BeB.jpg"
                    onerrer="https://img.kbs.co.kr/kbs/620/nsimg.kbs.co.kr/data/news/2010/01/08/2024781_BeB.jpg"
                    alt="Avatar image"
                >
                <div class="writer">
                    {{ writer }}
                </div>
                <div class="d-flex">
                    <div
                        class="button-like mobile-only margin-right-5"
                        @click="likeHandler('p', id, iLoveIt)"
                    >
                        +{{ likeCount }}
                    </div>
                    <div
                        class="button-dislike mobile-only margin-right-5"
                        @click="likeHandler('m', id, iLoveIt)"
                    >
                        -{{ unlikeCount }}
                    </div>
                    <div class="write-date">
                        {{ writeDate }}
                    </div>
                </div>
            </div>

            <div class="comment-body-container">
                <template
                    v-if="isUpdate"
                >
                    <div
                        class="comment-body padding-right-view"
                    >
                        <editor
                            ref="commentUpdateEditor"
                            :options="editorOpts"
                            :initial-value="content"
                            initial-edit-type="wysiwyg"
                        />
                    </div>
                    <div class="comment-body-sidebar">
                        <div 
                            class="btn btn-info comment-update-btn"
                            @click="modifyComment"
                        >
                            수정
                        </div>
                        <span class="comment-update-secret">
                            <input 
                                id="commentupdatesecret" 
                                v-model="commentSecret"
                                type="checkbox"
                                name="commentupdatesecret"
                                class="comment-update-secret-checkbox"
                            >
                            <label
                                for="commentupdatesecret"
                                class="comment-update-secret-label"
                            >
                                <span v-if="commentSecret">
                                    익명
                                </span>
                                <span v-else>
                                    닉네임공개
                                </span>
                            </label>
                        </span>
                    </div>
                </template>
                <template v-else>
                    <div class="comment-body comment-body-view padding-right-view">
                        <viewer
                            :initial-value="content"
                        />
                    </div>
                    <div class="comment-body-sidebar">
                        <div
                            class="button-like wide-only"
                            @click="likeHandlerWrapper('p', id, iLoveIt)"
                        >
                            <i
                                class="fa-thumbs-up up inline"
                                :class="{far: (iLoveIt <= 0), fa: (iLoveIt > 0)}"
                            /> {{ likeCount }}
                        </div>
                        <div
                            class="button-dislike wide-only"
                            @click="likeHandlerWrapper('m', id, iLoveIt)"
                        >
                            <i
                                class="fa-thumbs-down down inline"
                                :class="{far: (iLoveIt >= 0), fa: (iLoveIt < 0)}"
                            /> {{ unlikeCount }}
                        </div>
                        <b-modal
                            :id="`additional-like-modal-${isPost}-${id}`"
                            ref="modal"
                            title="더 좋아요를 누르시겠습니까?"
                        >
                            <span>
                                포인트 N을 소모해서 좋아요 한개를 더 추가하실 수 있습니다.
                            </span>
                            <template v-slot:modal-footer>
                                <b-button
                                    variant="outline-secondary"
                                    size="sm"
                                    class="float-right"
                                    @click="show=false"
                                >
                                    취소
                                </b-button>
                                <b-button
                                    variant="danger"
                                    size="sm"
                                    class="float-right"
                                    @click="show=false; likeHandler('p', id, iLoveIt, true)"
                                >
                                    좋아요 삭제
                                </b-button>
                                <b-button
                                    variant="primary"
                                    size="sm"
                                    class="float-right"
                                    @click="likeHandler('pp', id, iLoveIt, false)"
                                >
                                    더 좋아요
                                </b-button>
                            </template>
                        </b-modal>
                        <b-modal
                            :id="`additional-like-cancel-modal-${isPost}-${id}`"
                            ref="modal"
                            title="더 좋아요를 취소하시겠습니까?"
                        >
                            <span>
                                더 좋아요를 취소하시더라도 포인트는 반환되지 않습니다.
                            </span>
                            <template v-slot:modal-footer>
                                <b-button
                                    variant="outline-secondary"
                                    size="sm"
                                    class="float-right"
                                    @click="show=false"
                                >
                                    취소
                                </b-button>
                                <b-button
                                    variant="danger"
                                    size="sm"
                                    class="float-right"
                                    @click="show=false; likeHandler('pp', id, iLoveIt, true)"
                                >
                                    좋아요 삭제
                                </b-button>
                            </template>
                        </b-modal>
                    </div>
                </template>
            </div>
        </div>
    </div>
</template>

<script>
import SvgExclamation from '@/components/general/SvgMaterialReportProblem.vue';
import SvgPencil from '@/components/general/SvgMaterialRateReview.vue';
import SvgAuthor from '@/components/general/SvgMaterialVerifiedUser.vue';
import SvgDelete from '@/components/general/SvgMaterialBackspace.vue';


export default {
    name: "CounselDetailComment",
    components: {
        SvgAuthor,
        SvgDelete,
        SvgPencil,
        SvgExclamation,
    },
    props: {
        iLoveIt: {
            type: Number,
            required: true,
        },
        id: {
            type: Number,
            default: 0,
        },
        secret: {
            type: Boolean,
            required: true,
        },
        writer: {
            type: String,
            required: true,
        },
        writeDate: {
            type: String,
            required: true,
        },
        title: {
            type: String,
            required: false,
            default: '',
        },
        content: {
            type: String,
            required: true,
        },
        isPost: {
            type: Boolean,
            default: false,
        },
        isMine: {
            type: Boolean,
            default: false,
        },
        isAuthor: {
            type: Boolean,
            default: false,
        },
        likeCount: {
            type: Number,
            required: true,
        },
        unlikeCount: {
            type: Number,
            required: true,
        },
        likeHandler: {
            type: Function,
            required: true,
        },
        deleteHandler: {
            type: Function,
            required: true,
        },
        modifyHandler: {
            type: Function,
            required: true,
        },
        reportHandler: {
            type: Function,
            required: true,
        },
    },
    data(){
        return {
            isUpdate: false,
            commentSecret:null,
            editorOpts: null,
        };
    },
    computed: {
        nickname: {
            get() {
                return this.$store.getters.nickname;
            },
        },
    },
    mounted() {
        this.editorOpts = this.$store.getters.EDITOROPTIONS.options;
        this.commentSecret = this.secret;
    },
    methods: {
        changeUpdate(){
            this.isUpdate = !this.isUpdate;
        },
        modifyComment(){
            const htmlText = this.$refs.commentUpdateEditor.invoke("getHtml");
            this.modifyHandler(htmlText,this.id,this.commentSecret);
        },
        likeHandlerWrapper(type, id, iLoveIt) {
            if (type === 'p') {
                switch(iLoveIt) {
                case 2:
                    this.$bvModal.show(`additional-like-cancel-modal-${this.isPost}-${id}`);
                    break;
                case 1:
                    this.$bvModal.show(`additional-like-modal-${this.isPost}-${id}`);
                    break;
                case 0:
                case -1:
                    this.likeHandler('p', id, iLoveIt, false);
                    break;
                }
            } else {
                switch(iLoveIt) {
                case -1:
                    this.likeHandler('m', id, iLoveIt, true);
                    break;
                case 0:
                case 1:
                case 2:
                    this.likeHandler('m', id, iLoveIt, false);
                    break;
                }
            }
        },
        handleOK() {},
        handleCancel() {},
    },
};
</script>

<style lang="scss" scoped>
* {
    box-sizing: border-box;
    display: block;
}

.wide-only {
    display: none !important;
}

.margin-right-5 {
    margin-right: 5px;
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

.title-container {
    padding-left: 16px;
    padding-right: 8px;
    padding-bottom: 3px;
    padding-top: 3px;
    display: flex;
    align-items: center;
    background-color: whitesmoke;
    border-bottom: 1px gainsboro solid;
    border-top-right-radius: 6px;
    border-top-left-radius: 6px;
    flex-direction: row-reverse;
    
    .title-text {
        flex: 1 1 auto;
    }

    .buttons {
        display: flex;
        float: right;
        flex-shrink: 0 !important;
    }
    .button-item {
        margin-left: 4px;
        display: inline-block;
    }
    .button-item:hover {
        cursor: pointer;
    }
}

.avatar-container {
    z-index: 1;
    display: flex;
    box-sizing: border-box;
    align-items: center;
    border-bottom: 1px dotted lightgray;
    
    .avatar {
        margin: 4px;
        border-radius: 50% !important;
        height: 24px;
        width: 24px;
    }
    .writer {
        flex: 1;
        word-break: break-all;
    }
    .write-date {
        text-align: right;
        flex: 1;
        color: gray;
    }
    
    .button-like {
        color: green;
    }
    .button-dislike {
        color: brown;
    }
}

.comment-body-container {
    display: flex;
    flex-wrap: wrap;
    flex-direction: column;
    align-items: center;

    .comment-body {
        text-align: justify;
        overflow: visible;
        width: 100%;
        word-break: break-all;
    }
    .comment-body-view {
        margin-right: 0px !important;
        padding: 15px;
    }
}

.comment-body-sidebar {
    display: flex;
    flex-direction: column-reverse;
    text-align: center;
    align-items: center;

    .comment-update-btn {
        font-size: 100%;
    }
    .comment-update-secret {
        display: flex;
        align-items: center;
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
    .avatar-container {
        top: 0px;
        max-width: 76px;
        left: -92px;
        position: absolute;
        z-index: 1;
        display: block;
        box-sizing: border-box;
        text-align: center;
        border-bottom: unset;

        .avatar {
            border-radius: 50% !important;
            height: 60px;
            width: 60px;
            margin-bottom: 12px;
        }
        .writer {
            width: 100%;
            word-break: break-all;
        }
        .write-date {
            text-align: unset;
            color: gray;
            font-weight: 100;
        }
    }

    .title-container {
        .button-author {
            display: inline-block;
            border: 1.5px gray solid;
            color: #707070;
            border-radius: 2em;
            padding-top: 0.5px;
            padding-right: 4px;
            padding-left: 5px;
            font-size: 14px;
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
                top: 36px;
                font-size: 100%;
                width: unset;
            }
            
            .comment-update-secret{
                position: absolute;
                right: 9px;
                top: 80px;
                display: flex;
                align-items: center;
                font-size: 100%;
                
                .comment-update-secret-checkbox {
                    width: auto;
                }

                .comment-update-secret-label {
                    margin-bottom: 0.1rem;
                    margin-left: 2px;
                }
            }

            .button-like {
                float: right;
                position: absolute;
                right: 16px;
                top: 60px;
                padding-bottom: 2px;
                border-bottom: 1px solid lightgray;
                color: green;
                font-size: 100%;
            }
            .button-dislike {
                float: right;
                position: absolute;
                right: 16px;
                top: 90px;
                color: brown;
                font-size: 100%;
            }
            .inline {
                display: inline;
            }
        }
    }
}
</style>