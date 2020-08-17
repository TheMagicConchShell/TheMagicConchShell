<template>
    <div>
        <template v-if="list && list.length">
            <table class="table">
                <colgroup>
                    <col width="8%">
                    <col width="32%">
                    <col width="20%">
                    <col width="10%">
                    <col width="10%">
                    <col width="10%">
                    <col width="10%">
                </colgroup>
                <thead class="thead-light">
                    <tr>
                        <th>
                            순위
                        </th>
                        <th>
                            제목
                        </th>
                        <th>
                            작성자
                        </th>
                        <th
                            @click="changeSort(6)"
                        >
                            추천수
                            <i 
                                v-if="mySort==6" 
                                class="fas fa-long-arrow-alt-down"
                            />
                            <i
                                v-else
                                class="fas fa-sort"
                            />
                        </th>
                        <th
                            @click="changeSort(7)"
                        >
                            조회수
                            <i 
                                v-if="mySort==7" 
                                class="fas fa-long-arrow-alt-down"
                            />
                            <i
                                v-else
                                class="fas fa-sort"
                            />
                        </th>
                        <th
                            @click="changeSort(8)"
                        >
                            답변수
                            <i 
                                v-if="mySort==8" 
                                class="fas fa-long-arrow-alt-down"
                            />
                            <i
                                v-else
                                class="fas fa-sort"
                            />
                        </th>
                        <th>
                            작성날짜
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr 
                        v-for="(item,index) in list"
                        :key="item.nickname"
                    >
                        <td>{{ index+1 }}</td>
                        <td>
                            <router-link
                                :to="{name: 'CounselDetail', params: {no: item.no}}"
                            >
                                [{{ item.category }}] {{ item.title }}
                            </router-link>
                        </td>
                        <td>{{ item.writer }}</td>
                        <td>{{ item.likeCount }}</td>
                        <td>{{ item.views }}</td>
                        <td>{{ item.replyCount }}</td>
                        <td>{{ item.writeDate }}</td>
                    </tr>
                </tbody>
            </table>
        </template>
    </div>
</template>

<script>
export default {
    data() {
        return {
            list:[],
            mySort:6
        };
    },
    created() {
        this.getList(6);
    },
    methods: {
        getList(mySort){
            this.$axios({
                url:"/rank/post",
                method:"GET",
                params:{
                    sort:mySort
                }
            }).then((res)=>{
                if(res.data.status==="S-200"){
                    let formatDate = function (date) {
                        let d = new Date(date),
                            month = '' + (d.getMonth() + 1),
                            day = '' + d.getDate(),
                            year = d.getFullYear();
                        if (month.length < 2) 
                            month = '0' + month;
                        if (day.length < 2) 
                            day = '0' + day;
                        return [month, day].join('-');
                    };
                    this.list = res.data.data.map((e)=>{
                        e.writeDate = formatDate(e.writeDate);
                        e.category = this.$store.getters.categoryNameById(e.categoryId);
                        return e;
                    });
                }
            }).catch(e=>{
                console.log(e);
            });
        },
        changeSort(val){
            this.mySort = val;
            this.getList(val);
        },
    },
};
</script>

<style>
.table{
    table-layout: fixed;
}
.table td{
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
</style>