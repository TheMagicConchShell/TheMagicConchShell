<template>
    <div>
        <h2 id="categoryspace">
            카테고리 목록
        </h2>
        <template>
            <div class="text-right">
                <b-button
                    class="btn-danger"
                    @click.prevent="openCreateModal()"
                >
                    카테고리 추가
                </b-button>
            </div>
        </template>
        <br>
        <template v-if="categories && categories.length">
            <table
                id="category-list"
                class="table"
            >
                <colgroup>
                    <col width="15%">
                    <col width="20%">
                    <col width="45%">
                    <col width="10%">
                    <col width="10%">
                </colgroup>
                <thead class="thead-dark">
                    <tr>
                        <th
                            scope="col"
                        >
                            카테고리 번호
                        </th>
                        <th
                            scope="col"
                        >
                            카테고리 명
                        </th>
                        <th scope="col">
                            카테고리 설명
                        </th>
                        <th
                            scropt="col"
                            colspan="2"
                        />
                    </tr>
                </thead>
                <tbody>
                    <tr 
                        v-for="item in categories"
                        :key="item.id"
                    >
                        <td>{{ item.id }}</td>
                        <td>
                            {{ item.name }}
                        </td>
                        <td>
                            {{ item.description }}
                        </td>
                        <td>
                            <b-button @click.prevent="openUpdateModal(item.id, item.name, item.description)">
                                변경
                            </b-button>
                        </td>
                        <td>
                            <b-button @click.prevent="openDeleteModal(item.name)">
                                삭제
                            </b-button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </template>

        <template v-else>
            <td>
                로딩중입니다..
            </td>
        </template>
        

        <b-modal
            id="modal-update-category"
            ref="modal"
            title="카테고리를 수정 하시겠습니까?"
            @ok="handleOk"
        >
            <form 
                ref="updateform" 
                @submit.stop.prevent="updateCategory"
            >
                <b-form-group
                    :state="nameState"
                    label="이름"
                    label-for="name-input"
                >
                    <b-form-input
                        id="name-input"
                        v-model="changeName"
                        :state="changeNameState"
                        required  
                    />
                </b-form-group>
                <b-form-group
                    :state="descriptionState"
                    label="설명"
                    label-for="description-input"
                    invalid-feedback="설명을 입력해주세요."
                >
                    <b-form-input
                        id="description-input"
                        v-model="description"
                        :state="descriptionState"
                        required
                    />
                </b-form-group>
            </form>
        </b-modal>

        <b-modal
            id="modal-delete-category"
            ref="modal-delete"
            title="카테고리를 삭제하시겠습니까?"
            @ok="deleteOk"
        >
            <p>카테고리를 삭제 하시겠습니까?</p>
        </b-modal>

        <b-modal
            id="modal-create-category"
            ref="modal"
            title="카테고리를 추가 하시겠습니까?"
            @ok="createOk"
        >
            <form 
                ref="createform" 
                @submit.stop.prevent="createCategory"
            >
                <b-form-group
                    :state="nameState"
                    label="이름"
                    label-for="name-input"
                >
                    <b-form-input
                        id="name-input"
                        v-model="name"
                        :state="nameState"
                        required  
                    />
                </b-form-group>
                <b-form-group
                    :state="descriptionState"
                    label="설명"
                    label-for="description-input"
                    invalid-feedback="설명을 입력해주세요."
                >
                    <b-form-input
                        id="description-input"
                        v-model="description"
                        :state="descriptionState"
                        required
                    />
                </b-form-group>
            </form>
        </b-modal>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import moment from 'moment';
export default {
    data:()=>({
        name: '',
        changeName:'',
        description: '',
    }),
    computed: {
        ...mapGetters([
            'nickname',
            'categories'
        ]),
        nameState() {
            return this.name.length > 0;
        },
        descriptionState() {
            return this.description.length > 0;
        },
        changeNameState() {
            return this.changeName.length > 0;
        }
    },
    watch:{
        page(){
            
        },
    },
    created() {
        this.$store.dispatch('fetchCategories');
    },
    methods: {
        checkFormValidity() {
            const valid = this.$refs.updateform.checkValidity();           
            return valid;
        },
        openUpdateModal(id, name, desc){
            this.id = id;
            this.name = name;
            this.changeName = name;
            this.description = desc;
            this.$bvModal.show('modal-update-category');
        },
        handleOk(bvModalEvt){
            bvModalEvt.preventDefault();
            this.updateCategory();
        },
        updateCategory(){
            if(!this.checkFormValidity()){
                let msg = "모든 입력 항목을 입력해주세요.";
                this.$toast('안내', msg);
                return;
            }
            this.$store.dispatch('updateCategory', {
                source: this.name,
                destination: this.changeName,
                description: this.description,
            })
                .then((response) => {
                    this.$toast('안내', '카테고리 내용 수정 완료');
                })
                .catch((error) => {
                    console.log(error);
                });

            this.$nextTick(() =>{
                this.$bvModal.hide('modal-update-category');
            });
        },
        openDeleteModal(name){
            this.$bvModal.show('modal-delete-category');
            this.name = name;
        },
        deleteOk(bvModalEvt){
            bvModalEvt.preventDefault();
            this.deleteCategory();
        },
        deleteCategory(){
            this.$store.dispatch('updateCategory', {
                categoryName: this.name,
            })
                .then((response) => {
                    this.$toast('안내', '카테고리 내용 삭제 완료');
                    this.$router.go();
                })
                .catch((error) => {
                    console.log(error);
                });

            this.$nextTick(() =>{
                this.$bvModal.hide('modal-delete-category');
            });
        },
        checkCreateFormValidity() {
            const valid = this.$refs.createform.checkValidity();           
            return valid;
        },
        openCreateModal(){
            this.name = '';
            this.description = '';
            this.$bvModal.show('modal-create-category');
        },
        createOk(bvModalEvt){
            bvModalEvt.preventDefault();
            this.createCategory();
        },
        createCategory(){
            if(!this.checkCreateFormValidity()){
                let msg = "모든 입력 항목을 입력해주세요.";
                this.$toast('안내', msg);
                return;
            }
            this.$store.dispatch('updateCategory', {
                name : this.name,
                description: this.description,
            })
                .then((response) => {
                    this.$toast('안내', '카테고리 추가 완료');
                    this.$router.go();
                })
                .catch((error) => {
                    console.log(error);
                });

            this.$nextTick(() =>{
                this.$bvModal.hide('modal-create-category');
            });
        },

    },
};
</script>

<style scoped>
table {
    table-layout: fixed; 
    width: 100%;
}
td {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap; 
}
#recommendspace{
    text-align: center;
}
</style>