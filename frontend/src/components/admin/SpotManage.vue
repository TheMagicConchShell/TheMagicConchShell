<template>
    <div>
        <h2 id="categoryspace">
            Spot 목록
        </h2>
        <br>
        <template v-if="spotList && spotList.length">
            <table
                id="category-list"
                class="table"
            >
                <colgroup>
                    <col width="10%">
                    <col width="10%">
                    <col width="35%">
                    <col width="35%">
                    <col width="10%">
                </colgroup>
                <thead class="thead-dark">
                    <tr>
                        <th
                            scope="col"
                        >
                            Spot 번호
                        </th>
                        <th
                            scope="col"
                        >
                            고민 번호
                        </th>
                        <th scope="col">
                            등록 날짜
                        </th>
                        <th scope="col">
                            만료 날짜
                        </th>
                        <th />
                    </tr>
                </thead>
                <tbody>
                    <tr 
                        v-for="item in spotList"
                        :key="item.id"
                    >
                        <td>{{ item.id }}</td>
                        <td>
                            {{ item.postNo }}
                        </td>
                        <td>
                            {{ getFormatDate(item.uploadDate) }}
                        </td>
                        <td>
                            {{ getFormatDate(item.expireDate) }}
                        </td>
                        <td>
                            <b-button
                                class="btn-danger pull-right"
                                @click.prevent="openDeleteModal(item.id)"
                            >
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

        <template v-if="spotList && spotList.length">
            <div class="overflow-auto">
                <b-pagination
                    v-model="page"
                    :total-rows="rows"
                    :per-page="perPage"
                    first-text="First"
                    prev-text="Prev"
                    next-text="Next"
                    last-text="Last"
                    align="center"
                    aria-controls="allowed-counsel-list"
                />
            </div>
        </template>

        <b-modal
            id="modal-delete-spot"
            ref="modal-delete"
            title="Spot을 삭제하시겠습니까?"
            @ok="deleteOk"
        >
            <p>Spot을 삭제 하시겠습니까?</p>
        </b-modal>
    </div>
</template>

<script>
import {mapGetters} from 'vuex';
import moment from 'moment';
export default {
    data:()=>({
        spotList:[],
        id: '',
        page:1,
        rows:1,
        perPage:10,
    }),
    computed:{
        ...mapGetters(['nickname'])
    },
    created() {
        this.fetchSpotList(this.page);
    },
    methods: {
        async fetchSpotList(page){
            const response = await this.$axios({
                method: 'get',
                url: '/spot',
                headers:{
                    nickname : this.nickname,
                },
                params:{
                    page:page || 1,
                }
            }).then((res)=>{
                console.dir(res);
                if(res.status >= 200 && res.status < 300 ){
                    this.spotList = res.data.data.content;
                    this.rows = res.data.data.content.totalElements;
                    this.page = res.data.data.content.size;
                }
            }).catch((error)=>{
                console.log(error.response);
            });
        },
        getFormatDate(date) {
            return moment(new Date(date)).format("YYYY.MM.DD hh:mm:ss");
        },
        openDeleteModal(id){
            this.$bvModal.show('modal-delete-spot');
            this.id = id;
        },
        deleteOk(bvModalEvt){
            bvModalEvt.preventDefault();
            this.deleteSpot();
        },
        deleteSpot(){
            this.$axios({
                url: '/spot',
                method: "delete",
                headers:{
                    nickname : this.nickname,
                },
                params:{
                    id : this.id,
                }
            }).then(()=>{
                let msg = "Spot 삭제 완료";
                this.$toast('안내', msg);
                this.$router.go();
            }).catch((error)=>{
                console.log(error.response);
            });

            this.$nextTick(() =>{
                this.$bvModal.hide('modal-delete-spot');
            });
        },
    }
};
</script>

<style>

</style>