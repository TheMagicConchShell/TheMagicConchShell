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
                            {{ $t('ranking.table.rank') }}
                        </th>
                        <th>
                            {{ $t('ranking.table.title') }}
                        </th>
                        <th>
                            {{ $t('ranking.table.writer') }}
                        </th>
                        <th
                            @click="changeSort(6)"
                        >
                            {{ $t('ranking.table.like') }}
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
                            {{ $t('ranking.table.views') }}
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
                            {{ $t('ranking.table.reply') }}
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
                            {{ $t('ranking.table.date') }}
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
import { mapGetters } from 'vuex';
export default {
    data() {
        return {
            list:[],
            mySort:6
        };
    },
    computed: {
        ...mapGetters(['language']),
    },
    watch: {
        language() {
            this.getList(this.mySort);
        },
    },
    created() {
        this.$store.dispatch('fetchCategories');
        this.getList(this.mySort);
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
                        e.category = this.$store.getters.categoryNameById(e.categoryId, this.language);
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