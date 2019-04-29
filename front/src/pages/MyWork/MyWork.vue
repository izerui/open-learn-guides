<template>
    <el-table :data="workList" stripe>
        <el-table-column prop="CourseName" label="课程名称"/>
        <el-table-column prop="ExerciseName" label="作业名称"/>
        <el-table-column prop="ExerciseName" label="起止时间">
            <template slot-scope="scope">
                <div>
                    {{scope.row.StartDate}} 至 {{scope.row.EndDate}}
                </div>
            </template>
        </el-table-column>
        <el-table-column prop="ExerciseName" label="提交次数">
            <template slot-scope="scope">
                <span>{{scope.row.SubmitCount}} / {{scope.row.MaxTimesOfTrying}} 次</span>
            </template>
        </el-table-column>
        <el-table-column prop="MaxScore" label="最高分"/>
        <el-table-column prop="MaxScore" label="操作" width="250px">
            <template slot-scope="scope">
                <div>
                    <el-button size="mini" @click="gotoDoWork(scope.row)">做作业</el-button>
                    <el-button size="mini">做作业</el-button>
                    <el-button size="mini">做作业</el-button>
                </div>
            </template>
        </el-table-column>
    </el-table>
</template>

<script>

    export default {
        name: 'MyWork',
        data() {
            return {
                workList: []
            };
        },
        created() {
            this.getMyWork();
        },
        methods: {
            async getMyWork() {
                const result = await this.$get("/myWork");
                var alist = new Array();
                result.forEach(s => {
                    s.Data.forEach(d => {
                        d.Count = s.Count;
                        d.CourseName = s.CourseName;
                        d.url = s.url;
                        alist.push(d);
                    });
                })
                this.workList = alist;
                console.log(this.workList);
            },
            async gotoDoWork(row) {
                console.log(row);
                const url = `http://learn.open.com.cn/StudentCenter/OnLineJob/Redirect?mode=1&courseExerciseID=${row.CourseExerciseID}&submitCount=${row.SubmitCount}&studentHomeworkId=${row.studentHomeworkId}`;
                const key = await this.$get("/getTestPagerKey",{url:encodeURI(url)});

                const answerUrl = `http://learn.open.com.cn/StudentCenter/OnlineJob/DoHomework?courseExerciseId=${row.CourseExerciseID}&key=${key}&studentHomeworkId=${row.studentHomeworkId}&_=${(new Date()).getTime()}`;

                console.log(answerUrl);
                // window.open(url,"_blank");
            }
        }
    };
</script>
