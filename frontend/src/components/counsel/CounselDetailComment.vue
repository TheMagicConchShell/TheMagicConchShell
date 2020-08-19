<template>
    <div class="comment-wrapper">
        <div class="comment text-left">
            <div class="title-container">
                <div class="buttons">
                    <div
                        v-if="isAuthor"
                        class="button-author wide-only cursor-default"
                    >
                        <span>AUTHOR</span>
                    </div>
                    <div
                        v-if="isAuthor"
                        class="mobile-only"
                    >
                        <svg-author />
                    </div>
                    
                    <div
                        v-if="isSelected"
                        class="button-item cursor-default"
                    >
                        <svg-check-box
                            :class="{selected: isSelected}"
                        />
                    </div>

                    <div
                        v-if="(isPost && isMine) || nickname == 'admin'"
                        class="button-item cursor-pointer"
                        @click="advertiseCaller"
                    >
                        <svg-advertise />
                    </div>

                    <template v-if="(nickname && isMine) || nickname == 'admin'">
                        <template v-if="isPost">
                            <div 
                                v-if="nickname"
                                class="button-item cursor-pointer"
                                @click="modifyHandler"
                            >
                                <svg-pencil />
                            </div>
                        </template>
                        <template v-else>
                            <div 
                                v-if="nickname"
                                class="button-item cursor-pointer"
                                @click="changeUpdate"
                            >
                                <svg-pencil />
                            </div>
                        </template>
                        <div
                            v-if="nickname"
                            class="button-item cursor-pointer"
                            :disabled="$wait.is('counsel-chunk')"
                            @click="deleteHandler(id)"
                        >
                            <svg-delete />
                        </div>
                    </template>
                    <template v-else>
                        <div
                            v-if="isPostOwner && !isSelected"
                            class="button-item cursor-pointer"
                            @click="selectHandler(id)"
                        >
                            <svg-check-box />
                        </div>
                        <div
                            class="button-item cursor-pointer"
                            :disabled="$wait.is('counsel-chunk')"
                            @click="reportHandler"
                        >
                            <svg-exclamation />
                        </div>
                    </template>
                </div>
                <div
                    v-if="isSelected"
                    class="title-text"
                >
                    [채택된 답변]
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
                    :src="profileImg? profileImg: require(`@/assets/images/default_profile.png`)"
                    alt="Avatar image"
                    @error="$event.target.src = require(`@/assets/images/default_profile.png`)"
                >
                <div class="writer-container">
                    <div
                        class="writer"
                    >
                        {{ writer }}
                    </div>
                    <div class="hash">
                        {{ hash }}
                    </div>
                </div>
                <div class="d-flex flex-1">
                    <div
                        class="button-like mobile-only margin-right-5 cursor-pointer"
                        :disabled="$wait.is('counsel-chunk')"
                        @click="likeHandlerWrapper('p', id, iLoveIt)"
                    >
                        <i
                            class="fa-thumbs-up up inline"
                            :class="{far: (iLoveIt <= 0), fa: (iLoveIt > 0)}"
                        />
                        {{ likeCount }}
                    </div>
                    <div
                        class="button-dislike mobile-only margin-right-5 cursor-pointer"
                        :disabled="$wait.is('counsel-chunk')"
                        @click="likeHandlerWrapper('m', id, iLoveIt)"
                    >
                        <i
                            class="fa-thumbs-down down inline"
                            :class="{far: (iLoveIt >= 0), fa: (iLoveIt < 0)}"
                        /> {{ unlikeCount }}
                    </div>
                    <!-- <div class="write-date">
                        {{ writeDate }}
                    </div> -->
                </div>
            </div>

            <div class="comment-body-wrapper">
                <template
                    v-if="isUpdate"
                >
                    <div class="comment-body-container">
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
                                :disabled="$wait.is('counsel-chunk')"
                                @click="modifyHandlerWrapper"
                            >
                                수정
                            </div>
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
                    </div>
                </template>
                <template v-else>
                    <div class="write-date">
                        {{ formatDate }}
                    </div>
                    <div class="comment-body-container">
                        <div class="comment-body comment-body-view padding-right-view">
                            <viewer
                                :initial-value="content"
                            />
                        </div>
                        <div class="comment-body-sidebar">
                            <div
                                class="button-like wide-only cursor-pointer"
                                :disabled="$wait.is('counsel-chunk')"
                                @click="likeHandlerWrapper('p', id, iLoveIt)"
                            >
                                <i
                                    class="fa-thumbs-up up inline"
                                    :class="{far: (iLoveIt <= 0), fa: (iLoveIt > 0)}"
                                /> {{ likeCount }}
                            </div>
                            <div
                                class="button-dislike wide-only cursor-pointer"
                                :disabled="$wait.is('counsel-chunk')"
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
                                <template v-slot:modal-footer="{ hide }">
                                    <b-button
                                        variant="outline-secondary"
                                        size="sm"
                                        class="float-right"
                                        @click="hide"
                                    >
                                        취소
                                    </b-button>
                                    <b-button
                                        variant="danger"
                                        size="sm"
                                        class="float-right"
                                        :disabled="$wait.is('counsel-chunk')"
                                        @click="likeHandler('p', id, iLoveIt, true)"
                                    >
                                        좋아요 삭제
                                    </b-button>
                                    <b-button
                                        variant="primary"
                                        size="sm"
                                        class="float-right"
                                        :disabled="$wait.is('counsel-chunk')"
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
                                <template v-slot:modal-footer="{ hide }">
                                    <b-button
                                        variant="outline-secondary"
                                        size="sm"
                                        class="float-right"
                                        :disabled="$wait.is('counsel-chunk')"
                                        @click="hide"
                                    >
                                        취소
                                    </b-button>
                                    <b-button
                                        variant="danger"
                                        size="sm"
                                        class="float-right"
                                        :disabled="$wait.is('counsel-chunk')"
                                        @click="likeHandler('pp', id, iLoveIt, true)"
                                    >
                                        좋아요 삭제
                                    </b-button>
                                </template>
                            </b-modal>
                        </div>
                    </div>
                </template>
            </div>
        </div>
    </div>
</template>

<script>
import moment from 'moment';
import { mapGetters } from 'vuex';
import SvgExclamation from '@/components/general/SvgMaterialReportProblem.vue';
import SvgPencil from '@/components/general/SvgMaterialRateReview.vue';
import SvgAuthor from '@/components/general/SvgMaterialVerifiedUser.vue';
import SvgDelete from '@/components/general/SvgMaterialBackspace.vue';
import SvgCheckBox from '@/components/general/SvgMaterialCheckBox.vue';
import SvgAdvertise from '@/components/general/SvgMaterialCampaign.vue';

export default {
    name: "CounselDetailComment",
    components: {
        SvgAuthor,
        SvgDelete,
        SvgPencil,
        SvgExclamation,
        SvgCheckBox,
        SvgAdvertise,
    },
    props: {
        hash: {
            type: String,
            default: '',
        },
        isSelected: {
            type: Boolean,
            default: false,
        },
        isPostOwner: {
            type: Boolean,
            required: true,
        },
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
        profileImg: {
            type: String,
            required: false,
            default: null,
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
        selectHandler: {
            type: Function,
            default: () => {},
        },
        advertiseCaller: {
            type: Function,
            default: () => {},
        }
    },
    data(){
        return {
            isUpdate: false,
            commentSecret:null,
            editorOpts: null,
        };
    },
    computed: {
        ...mapGetters(['nickname']),
        formatDate() {
            return moment(new Date(this.writeDate)).format("YYYY.MM.DD HH:mm:ss");
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
        modifyHandlerWrapper(){
            const htmlText = this.$refs.commentUpdateEditor.invoke("getHtml");
            this.modifyHandler(htmlText,this.id,this.commentSecret);
        },
        likeHandlerWrapper(type, id, iLoveIt) {
            if (!this.nickname) {
                this.$toast('좋아요', '좋아요/싫어요를 누르시려면 로그인해주세요.');
                return;
            }
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
    },
};
</script>

<style lang="scss" scoped>
.hash {
    color: dimgray
}
*[disabled] {
    opacity: 0.4;
}
* {
    box-sizing: border-box;
    display: block;
}
.selected {
    color: green;
}
.inline {
    display: inline;
}
.cursor-default:hover {
    cursor: default;
}
.cursor-pointer:hover {
    cursor: pointer;
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
    .writer-container {
        flex: 1;
        display: flex;
        flex-direction: row;

        .writer {
            word-break: break-all;
            margin-right: 2px;
        }
    }
    
    .button-like {
        color: green;
    }
    .button-dislike {
        color: brown;
    }
}

.comment-body-wrapper {
    display: flex;
    flex-wrap: wrap;
    flex-direction: column;
    align-items: center;

    .write-date {
        float: right;
        flex: 1;
        color: gray;
        width: 100%;
        text-align: right;
        padding-right: 12px;
    }

    .comment-body-container {
        display: flex;
        width: 100%;
    }

    .comment-body {
        text-align: justify;
        overflow: visible;
        width: 100%;
        word-break: break-all;
    }
    .comment-body-view {
        margin-right: 0px !important;
        padding: 0px 15px 15px 15px;
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
        width: 126px;
        left: -126px;
        position: absolute;
        z-index: 1;
        display: flex;
        flex-direction: column;
        box-sizing: border-box;
        text-align: center;
        border-bottom: unset;

        .avatar {
            border-radius: 50% !important;
            height: 60px;
            width: 60px;
            margin-bottom: 12px;
        }

        .writer-container {
            flex: 1;
            display: flex;
            flex-direction: column;

            .writer {
                width: 100%;
                word-break: break-all;
            }
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

    .comment-body-wrapper {
        display: flex;

        .write-date {
            text-align: right;
            color: gray;
            font-weight: 100;
            width: 100%;
            padding-right: 6px;
        }

        .comment-body-container {
            display: flex;
            width: 100%;

            .comment-body {
                flex: 1;
            }
            .comment-body-sidebar {
                width: 60px;

                .button-like {
                    padding-bottom: 2px;
                    border-bottom: 1px solid lightgray;
                    color: green;
                    font-size: 100%;
                }
                .button-dislike {
                    color: brown;
                    font-size: 100%;
                }
            }
        }
        
        .comment-body-sidebar {
            display: flex;
            flex-direction: column;

            .comment-update-btn{
                position: absolute;
                right: 4px;
                top: 36px;
                font-size: 100%;
                width: unset;
            }
            
            .comment-update-secret{
                display: flex;
                flex-direction: column;
                
                position: absolute;
                right: 16px;
                top: 80px;
                display: flex;
                align-items: center;
                font-size: 75%;
                
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