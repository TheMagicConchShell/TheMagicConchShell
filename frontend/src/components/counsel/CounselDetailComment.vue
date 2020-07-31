<template>
    <div class="comment">
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
            <div class="write-date">
                {{ writeDate }}
            </div>
        </div>
        <div class="comment-wrapper">
            <div class="text-left comment-container">
                <div class="comment-expand">
                    <div class="title">
                        <div class="buttons">
                            <div
                                v-if="isAuthor"
                                class="button-item button-author"
                            >
                                AUTHOR
                            </div>

                            <template v-if="isMine">
                                <div
                                    class="button-item"
                                    @click="deleteHandler"
                                >
                                    <svg-delete />
                                </div>
                                <div 
                                    v-if="!isPost"
                                    class="button-item"
                                    @click="changeUpdate"
                                >
                                    <svg-pencil />
                                </div>
                                <div
                                    v-else
                                    class="button-item"
                                    @click="modifyHandler"
                                >
                                    <svg-pencil />
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
                    <table class="content">
                        <tbody>
                            <template v-if="!isPost&&isUpdate">
                                <td class="comment-body">
                                    <editor
                                        :initial-value="content"
                                        initial-edit-type="wysiwyg"
                                        height="150px"
                                    />
                                </td>
                                <td 
                                    class="btn btn-info comment-update-btn"
                                    @click="modifyHandler"
                                >
                                    수정
                                </td>
                            </template>
                            <template v-else>
                                <td class="comment-body">
                                    <viewer
                                        :initial-value="content"
                                    />
                                </td>
                                <td
                                    class="comment-side-up"
                                    @click="upHandler('p')"
                                >
                                    +{{ likeCount }}
                                </td>
                                <td
                                    class="comment-side-down"
                                    @click="upHandler('m')"
                                >
                                    -{{ unlikeCount }}
                                </td>
                            </template>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import SvgExclamation from '@/components/general/SvgExclamation.vue';
import SvgPencil from '@/components/general/SvgPencil.vue';
import SvgDelete from '@/components/general/SvgDelete.vue';


export default {
    name: "CounselDetailComment",
    components: {
        SvgDelete,
        SvgPencil,
        SvgExclamation,
    },
    props: {
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
        upHandler: {
            type: Function,
            required: true,
        },
        downHandler: {
            type: Function,
            required: true,
        },
        deleteHandler: {
            type: Function,
            default: null,
        },
        modifyHandler: {
            type: Function,
            default: null,
        },
        reportHandler: {
            type: Function,
            default: null,
        },
    },
    data(){
        return {
            isUpdate:false
        };
    },
    methods: {
        changeUpdate(){
            this.isUpdate = !this.isUpdate;
        }
    },
};
</script>

<style scoped>
* {
    box-sizing: border-box;
    display: block;
}
.comment {
    position: relative;
    display: flex;
    width: 75%;
    left: -16px;
    margin-bottom: 22px;
    min-height: 160px;
}
.avatar-container {
    max-width: 76px;
    left: -92px;
    position: absolute;
    z-index: 1;
    display: block;
    box-sizing: border-box;
}
.writer {
    width: 100%;
    word-break: break-all;
}
.write-date {
    color: gray;
    font-weight: 100;
}

.comment-wrapper {
    position: relative;
    border: 1px gray solid;
    flex: auto;
    border-radius: 6px;
    max-width: 100%;
}

.comment-container {
    margin-left: 16px;
    margin-right: 60px;
}

.avatar {
    border-radius: 50% !important;
    height: 60px;
    width: 60px;
    margin-bottom: 12px;
}

.title {
    padding-left: 16px;
    padding-right: 16px;
    padding-bottom: 3px;
    padding-top: 3px;
    display: flex;
    align-items: center;
    background-color: whitesmoke;
    border-bottom: 1px gainsboro solid;
    border-top-right-radius: 6px;
    border-top-left-radius: 6px;
    flex-direction: row-reverse;
}
.title-text {
    flex: 1 1 auto;
}
.content {
    padding-left: 16px;
    padding-right: 60px;
}

.comment-expand {
    margin-left: -16px !important;
    margin-right: -60px !important;
}

.comment-body {
    overflow: visible;
    width: 100%;
    margin-right: 0px !important;
    padding: 15px;
    word-break: break-all;
}
.comment-side-up {
    float: right;
    position: absolute;
    right: 28px;
    top: 60px;
    border-bottom: 1px solid gray;
    color: green;
    font-size: 140%;
}
.comment-side-down {
    float: right;
    position: absolute;
    right: 28px;
    top: 100px;
    color: brown;
    font-size: 140%;
}
.comment-update-btn{
    float: right;
    position: absolute;
    right: 13px;
    top: 60px;
    font-size: 100%;
}
.buttons {
    float: right;
    flex-shrink: 0!important;
}
.button-item {
    display: inline-block;
}
.button-author {
    display: inline-block;
    border: 1px gray solid;
    border-radius: 2em;
    padding-right: 5px;
    padding-left: 5px;
    font-size: 14px;
    font-weight: bold;
}

.menu {
    display: inline-block;
}
</style>