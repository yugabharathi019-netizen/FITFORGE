package com.fitforge.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject

private val Bg = Color(0xFF090D0B)
private val Panel = Color(0xFF151D19)
private val Green = Color(0xFFB7FF35)
private val Muted = Color(0xFF9AA99F)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("fitforge", MODE_PRIVATE)
        setContent {
            var page by remember { mutableStateOf("Home") }
            var gymMode by remember { mutableStateOf(true) }
            var calories by remember { mutableIntStateOf(prefs.getInt("cal", 0)) }
            var protein by remember { mutableFloatStateOf(prefs.getFloat("protein", 0f)) }
            var water by remember { mutableIntStateOf(prefs.getInt("water", 0)) }
            var streak by remember { mutableIntStateOf(prefs.getInt("streak", 0)) }
            var weight by remember { mutableStateOf(prefs.getString("weight", "60") ?: "60") }
            var height by remember { mutableStateOf(prefs.getString("height", "170") ?: "170") }
            var sleep by remember { mutableStateOf(prefs.getString("sleep", "8") ?: "8") }
            var foodQuery by remember { mutableStateOf("") }
            var serving by remember { mutableStateOf("100") }
            var workoutHistory by remember { mutableStateOf(prefs.getString("history", "") ?: "") }
            var measurements by remember { mutableStateOf(prefs.getString("measurements", "") ?: "") }
            var themeChoice by remember { mutableStateOf(prefs.getString("theme", "Neon Green") ?: "Neon Green") }
            val foods = listOf(
                Food("Idli",58,2.0), Food("Plain dosa",168,4.0), Food("Masala dosa",190,4.5),
                Food("Cooked rice",130,2.7), Food("Sambar",75,4.0), Food("Rasam",35,1.5),
                Food("Chapati",120,3.5), Food("Parotta",300,5.0), Food("Boiled egg",155,13.0),
                Food("Chicken breast cooked",165,31.0), Food("Fish cooked",140,24.0),
                Food("Paneer",265,18.0), Food("Milk",61,3.2), Food("Curd",98,3.5),
                Food("Banana",89,1.1), Food("Peanuts",567,26.0), Food("Cooked dal",116,9.0),
                Food("Chickpeas cooked",164,8.9), Food("Oats",389,17.0), Food("Pongal",150,4.0),
                Food("Upma",150,4.0), Food("Poha",130,2.5), Food("Apple",52,0.3),
                Food("Almonds",579,21.2), Food("Whey protein powder",400,80.0)
            )
            val accent = if (themeChoice == "Neon Blue") Color(0xFF45C9FF) else if (themeChoice == "Violet") Color(0xFFB58AFF) else Green
            MaterialTheme(colorScheme = darkColorScheme(primary = accent, background = Bg, surface = Panel)) {
                Surface(Modifier.fillMaxSize(), color = Bg) {
                    Column(Modifier.fillMaxSize().padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Column {
                                Text("FITFORGE", color = accent, fontSize = 26.sp, fontWeight = FontWeight.Black)
                                Text("TRAIN • EAT • EVOLVE", color = Muted, fontSize = 10.sp, letterSpacing = 2.sp)
                            }
                            Text("● OFFLINE READY", color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(14.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Home","Workout","Food","Tools","Habits","Profile").forEach { item ->
                                Box(Modifier.weight(1f).background(if(page==item) accent else Panel, RoundedCornerShape(10.dp)).clickable { page=item }.padding(vertical=10.dp), contentAlignment=Alignment.Center) {
                                    Text(item, color=if(page==item) Bg else Color.White, fontSize=10.sp, fontWeight=FontWeight.Bold)
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        when(page) {
                            "Home" -> LazyColumn(verticalArrangement=Arrangement.spacedBy(10.dp)) {
                                item { Text("YOUR DAILY COMMAND CENTER", color=Color.White, fontWeight=FontWeight.Bold, fontSize=18.sp) }
                                item { Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                                    StatCard("Calories", "$calories kcal", Modifier.weight(1f), accent)
                                    StatCard("Protein", "${"%.1f".format(protein)} g", Modifier.weight(1f), accent)
                                } }
                                item { Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                                    StatCard("Water", "$water / 8 glasses", Modifier.weight(1f), accent)
                                    StatCard("NoFap streak", "$streak days", Modifier.weight(1f), accent)
                                } }
                                item { CardBlock("QUICK ACTIONS", accent) {
                                    Button(onClick={page="Workout"}, modifier=Modifier.fillMaxWidth()) { Text("Start workout") }
                                    OutlinedButton(onClick={page="Food"}, modifier=Modifier.fillMaxWidth()) { Text("Log Indian food") }
                                } }
                                item { CardBlock("TODAY'S FOCUS", accent) {
                                    Text("Train consistently • Eat enough protein • Sleep well • Stay hydrated", color=Color.White)
                                } }
                            }
                            "Workout" -> LazyColumn(verticalArrangement=Arrangement.spacedBy(10.dp)) {
                                item { Text("TRAINING MODE", color=Color.White, fontSize=20.sp, fontWeight=FontWeight.Bold) }
                                item { Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                                    Button(onClick={gymMode=true}, modifier=Modifier.weight(1f), colors=ButtonDefaults.buttonColors(containerColor=if(gymMode) accent else Panel, contentColor=if(gymMode) Bg else Color.White)) { Text("GYM") }
                                    Button(onClick={gymMode=false}, modifier=Modifier.weight(1f), colors=ButtonDefaults.buttonColors(containerColor=if(!gymMode) accent else Panel, contentColor=if(!gymMode) Bg else Color.White)) { Text("HOME") }
                                } }
                                val list = if(gymMode) listOf("Bench press • 3 × 8–12","Lat pulldown • 3 × 10–12","Shoulder press • 3 × 10","Bicep curls • 3 × 12","Squats • 3 × 8–12","Leg press • 3 × 12","Romanian deadlift • 3 × 10") else listOf("Push-ups • 3 × 8–15","Bodyweight squats • 3 × 15","Reverse lunges • 3 × 10/leg","Plank • 3 × 30 sec","Glute bridge • 3 × 15","Mountain climbers • 3 × 30 sec","Jumping jacks • 3 × 30 sec")
                                items(list) { exercise ->
                                    var checked by remember { mutableStateOf(false) }
                                    Row(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(12.dp)).padding(12.dp), verticalAlignment=Alignment.CenterVertically) {
                                        Column(Modifier.weight(1f)) { Text(exercise, color=Color.White, fontWeight=FontWeight.SemiBold); Text(if(gymMode) "Log weight in your workout notes" else "Bodyweight exercise", color=Muted, fontSize=11.sp) }
                                        Checkbox(checked, { checked=it; if(it) { workoutHistory = (workoutHistory + "\n" + exercise).trim(); prefs.edit().putString("history",workoutHistory).apply() } }, colors=CheckboxDefaults.colors(checkedColor=accent))
                                    }
                                }
                                item { Text("Completed exercises are added to Workout History.", color=Muted, fontSize=11.sp) }
                                item { OutlinedButton(onClick={page="Profile"}, modifier=Modifier.fillMaxWidth()) { Text("View workout history") } }
                            }
                            "Food" -> LazyColumn(verticalArrangement=Arrangement.spacedBy(9.dp)) {
                                item { Text("INDIAN FOOD TRACKER", color=Color.White, fontSize=20.sp, fontWeight=FontWeight.Bold) }
                                item { Text("Approximate nutrition per 100 g. Actual values vary by recipe.", color=Muted, fontSize=11.sp) }
                                item { OutlinedTextField(foodQuery,{foodQuery=it},label={Text("Search food")},modifier=Modifier.fillMaxWidth()) }
                                item { OutlinedTextField(serving,{serving=it},label={Text("Serving size in grams")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Number),modifier=Modifier.fillMaxWidth()) }
                                items(foods.filter { it.name.contains(foodQuery,true) }) { f ->
                                    Row(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(12.dp)).padding(12.dp), verticalAlignment=Alignment.CenterVertically) {
                                        Column(Modifier.weight(1f)) {
                                            Text(f.name, color=Color.White, fontWeight=FontWeight.Bold)
                                            Text("${f.kcal} kcal • ${f.protein} g protein / 100g", color=Muted, fontSize=11.sp)
                                        }
                                        Button(onClick={
                                            val grams=(serving.toDoubleOrNull() ?: 100.0).coerceAtLeast(0.0)/100.0
                                            calories += (f.kcal*grams).toInt()
                                            protein += (f.protein*grams).toFloat()
                                            prefs.edit().putInt("cal",calories).putFloat("protein",protein).apply()
                                        }) { Text("ADD") }
                                    }
                                }
                                item { Button(onClick={calories=0;protein=0f;prefs.edit().putInt("cal",0).putFloat("protein",0f).apply()}, modifier=Modifier.fillMaxWidth()) { Text("Reset today's food totals") } }
                            }
                            "Tools" -> LazyColumn(verticalArrangement=Arrangement.spacedBy(10.dp)) {
                                item { Text("FITNESS CALCULATORS", color=Color.White, fontSize=20.sp, fontWeight=FontWeight.Bold) }
                                item { OutlinedTextField(weight,{weight=it},label={Text("Weight (kg)")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal),modifier=Modifier.fillMaxWidth()) }
                                item { OutlinedTextField(height,{height=it},label={Text("Height (cm)")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal),modifier=Modifier.fillMaxWidth()) }
                                val w=weight.toDoubleOrNull() ?: 0.0
                                val h=(height.toDoubleOrNull() ?: 0.0)/100.0
                                item { CardBlock("BMI",accent) { Text(if(w>0 && h>0) "%.1f".format(w/(h*h)) else "Enter valid height and weight", color=accent, fontSize=25.sp, fontWeight=FontWeight.Bold) } }
                                item { CardBlock("PROTEIN TARGET",accent) { Text("${(w*1.6).toInt()}–${(w*2.0).toInt()} g/day", color=accent, fontSize=23.sp, fontWeight=FontWeight.Bold); Text("General muscle-building estimate; individual needs vary.",color=Muted,fontSize=11.sp) } }
                                item { CardBlock("CALORIE ESTIMATE",accent) { Text("Use a validated TDEE calculator with age, sex and activity level for a more complete estimate.",color=Color.White) } }
                                item { CardBlock("WATER & SLEEP",accent) {
                                    Text("Water glasses today: $water",color=Color.White)
                                    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { Button(onClick={water++;prefs.edit().putInt("water",water).apply()}) { Text("+ Glass") }; OutlinedButton(onClick={water=(water-1).coerceAtLeast(0);prefs.edit().putInt("water",water).apply()}) { Text("- Glass") } }
                                    OutlinedTextField(sleep,{sleep=it;prefs.edit().putString("sleep",it).apply()},label={Text("Sleep hours")},modifier=Modifier.fillMaxWidth())
                                } }
                            }
                            "Habits" -> LazyColumn(verticalArrangement=Arrangement.spacedBy(10.dp)) {
                                item { Text("DISCIPLINE & RECOVERY", color=Color.White, fontSize=20.sp, fontWeight=FontWeight.Bold) }
                                item { CardBlock("NOFAP / HABIT STREAK",accent) {
                                    Text("$streak DAYS",color=accent,fontSize=32.sp,fontWeight=FontWeight.Black)
                                    Text("Self-reported streak counter. Progress is personal.",color=Muted,fontSize=11.sp)
                                    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                                        Button(onClick={streak++;prefs.edit().putInt("streak",streak).apply()}) { Text("+ 1 day") }
                                        OutlinedButton(onClick={streak=0;prefs.edit().putInt("streak",0).apply()}) { Text("Reset") }
                                    }
                                } }
                                item { CardBlock("WATER TRACKER",accent) {
                                    Text("$water glasses logged",color=Color.White)
                                    Button(onClick={water++;prefs.edit().putInt("water",water).apply()}) { Text("Add a glass") }
                                } }
                                item { CardBlock("SLEEP TRACKER",accent) {
                                    OutlinedTextField(sleep,{sleep=it;prefs.edit().putString("sleep",it).apply()},label={Text("Hours slept")},modifier=Modifier.fillMaxWidth())
                                } }
                                item { Text("Reminder scheduling and automated streak dates are not yet implemented in this first build.",color=Muted,fontSize=11.sp) }
                            }
                            "Profile" -> LazyColumn(verticalArrangement=Arrangement.spacedBy(10.dp)) {
                                item { Text("PROFILE & HISTORY", color=Color.White, fontSize=20.sp, fontWeight=FontWeight.Bold) }
                                item { CardBlock("BODY MEASUREMENTS",accent) {
                                    OutlinedTextField(weight,{weight=it;prefs.edit().putString("weight",it).apply()},label={Text("Weight kg")},modifier=Modifier.fillMaxWidth())
                                    OutlinedTextField(measurements,{measurements=it;prefs.edit().putString("measurements",it).apply()},label={Text("Waist / chest / arm notes")},modifier=Modifier.fillMaxWidth())
                                    Text("Progress photos: add through your phone's gallery in a future version.",color=Muted,fontSize=11.sp)
                                } }
                                item { CardBlock("WORKOUT HISTORY",accent) { Text(workoutHistory.ifBlank{"No completed exercises yet."},color=Color.White) } }
                                item { CardBlock("CUSTOM THEME",accent) {
                                    listOf("Neon Green","Neon Blue","Violet").forEach { t ->
                                        Row(verticalAlignment=Alignment.CenterVertically) { RadioButton(themeChoice==t,{themeChoice=t;prefs.edit().putString("theme",t).apply()}); Text(t,color=Color.White) }
                                    }
                                } }
                            }
                        }
                    }
                }
            }
        }
    }
}
data class Food(val name:String,val kcal:Int,val protein:Double)
@Composable fun StatCard(title:String,value:String,modifier:Modifier,accent:Color) {
    Column(modifier.background(Panel,RoundedCornerShape(14.dp)).padding(14.dp)) {
        Text(title,color=Muted,fontSize=11.sp)
        Text(value,color=accent,fontSize=20.sp,fontWeight=FontWeight.Black)
    }
}
@Composable fun CardBlock(title:String,accent:Color,content:@Composable ColumnScope.()->Unit) {
    Column(Modifier.fillMaxWidth().background(Panel,RoundedCornerShape(14.dp)).padding(14.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
        Text(title,color=accent,fontWeight=FontWeight.Bold,fontSize=12.sp,letterSpacing=1.sp)
        content()
    }
}
