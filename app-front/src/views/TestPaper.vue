<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-card tile>
        <v-toolbar card dark color="primary">
            <v-btn icon dark @click="$emit('close')">
                <v-icon>close</v-icon>
            </v-btn>
            <v-toolbar-title>{{titleLabel}}</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-toolbar-items>
                <v-btn dark flat @click="$emit('close')">保存答案</v-btn>
            </v-toolbar-items>
        </v-toolbar>

        <v-expansion-panel expand>
            <v-expansion-panel-content v-for="sec in sections">
                <template v-slot:header>
                    <div><h3>{{sec.Title}}</h3></div>
                </template>

                <v-card v-for="(qes,index) in sec.items">
                    <v-card-title primary-title>
                        <span class="title body-2">
                            {{index+1}}. {{qes.I2}}
                        </span>
                        <v-spacer></v-spacer>
                        <span class="title body-2">{{index+1}}. {{qes.ans}}</span>
                    </v-card-title>
                    <v-card-text class="headline">
                        <span class="grey--text body-2"
                              v-for="ch in qes.Choices">&nbsp;&nbsp;&nbsp;&nbsp;{{ch.I1}}. {{ch.I2}}<br></span>
                    </v-card-text>
                </v-card>
            </v-expansion-panel-content>
        </v-expansion-panel>
    </v-card>
</template>

<script>
    export default {
        name: "TestPaper",
        props: {
            paperJson: {
                type: Object,
                required: true
            }
        },
        computed: {
            titleLabel() {
                if (this.doneAnswer) {
                    return this.paperJson.ExerciseName;
                } else {
                    return this.paperJson.ExerciseName + "   -  (正在获取答案)...";
                }
            },
            sections() {
                var sections = this.paperJson.TestPaperContent.Sections;

                const questions = this.paperJson.TestPaperContent.Items;

                sections.forEach(s => {
                    if (!s.items) {
                        s.items = [];
                    }
                    s.ItemID.forEach(id => {
                        questions.forEach(ques => {
                            if (ques.I1 === id) {
                                s.items.push(ques);
                            }
                        })
                    })
                });
                console.log("包装后", sections);
                return sections;
            }
        }
    }
</script>

<style scoped>

</style>