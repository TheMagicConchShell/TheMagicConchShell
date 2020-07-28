<template>
    <div>
        <div class="container">
            <div>
                <editor ref="editorText" v-model="editorText" :options="editorOpts" initialEditType='wysiwyg' height="500px" />
                <button @click="regist">
                    등록하기
                </button>
            </div>
        </div>
    </div>
</template>
<script>
import 'codemirror/lib/codemirror.css';
import '@toast-ui/editor/dist/toastui-editor.css';
import '@toast-ui/editor/dist/toastui-editor-viewer.css';
import 'tui-color-picker/dist/tui-color-picker.css';


import { Editor } from '@toast-ui/vue-editor';
import colorSyntax from '@toast-ui/editor-plugin-color-syntax';



export default {
    components: {
        editor: Editor,
    },
    data () {
        return {
            editorText: '',
            content:``,
            urlSrc:'',
            editorOpts:{
                hooks:{
                    addImageBlobHook :  async (blob, callback)=>{
                        var src = window.URL.createObjectURL(blob);
                        console.log(src);
                        callback(src,"uploaded image");
                        return false;
                    }
                },
                plugins: [colorSyntax],
            },
            
        };
    },
    mounted() {
    },
    methods: {
        regist(){
            alert("등록완료");
        }
    },
};

</script>

<style>

</style>