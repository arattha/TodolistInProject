<template>
    <div>

    </div>
</template>

<script>
import { removeAlarm, removeAllAlarm } from "@/api/alarm.js";
import { mapGetters } from "vuex";

export default {
    name:"Alarm",
    data(){
        return{
            alarmList:[],
        }
    },
    created(){
        this.alarmList = this.$route.params.alarmList;
    },
    computed:{
        ...mapGetters(["id"])
    },
    methods:{
        check(alarmId){
            
            removeAlarm(
                alarmId,
                () => {
                    alert("삭제되었습니다.");
                },
                () => {}
            );

        },
        checkAll(){
            
            removeAllAlarm(
                this.id,
                () => {
                    alert("모두 삭제되었습니다.");
                },
                () => {}
            );

        },
        goTodo(todoId){
            // 해당 Todo의 상세 페이지로 이동
            // this.$route.push("/todoInfo");
        }
    }
}
</script>

<style>

</style>