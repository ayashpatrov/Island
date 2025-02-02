# Island

## Описание

<p>
    Это проект симуляции жизни на острове. Остров представляет собой двумерный массив локаций, на каждой локации может
размещаться 15 видов животных и 1 вид растений. Симуляция происходит в пошаговом режиме, за каждый шаг (такт), каждое
животное принимает решение, какое из 4 действий оно будет совершать и совершает его:
    <ul>
        <li>Переместится в другую локацию согласно настройке <a href="#speed-setting">creatureSettings.{CreatureType}.speed</a></li>
        <li>Поесть (Выбор пищи и акт поедания осуществляется согласно настройке <a href="#eating-chance-setting">creatureSettings.{CreatureType}.eatingChance</a>)</li>
        <li>Размножиться (Колчичество рождаемых потомков задаётся в настройке <a href="#litterCount-setting">creatureSettings.{CreatureType}.litterCount</a>)</li>
        <li>Умереть с голоду</li>
    </ul>
Растения безусловно растут каждый такт, если на клетке представлена хотя бы одна особь растения.

Симуляция останавливается после выполнения количества тактов, заданного в настройке <a href="#beat-count-setting">lifeCycleSettings.beatCount</a>
<p>

## Настройки

Настройки симуляции задаются в файле ```src/main/resources/CommonSettings.json```
<p/>
Все настройки можно разделить на три группы:
<ul>
    <li><a href="#island-settings">islandSettings</a> - настройки острова</li>
    <li><a href="#life-cycle-settings">lifeCycleSettings</a> - настройки "жизни" на острове</li>
    <li><a href="#location-settings">locationSettings</a> - настройки локации острова, сколько и кого может разместиться на одной локации</li>
</ul>

### islandSettings

<table id="island-settings">
    <tbody>
        <tr>
            <th>Настройка</th>
            <th>Описание</th>
        </tr>
        <tr>
            <td>islandSettings.rowsCount</td>
            <td>"Высота" острова</td>
        </tr>
        <tr>
            <td>islandSettings.columnsCount</td>
            <td>"Ширина" острова</td>
        </tr>
    </tbody>
</table>

### lifeCycleSettings
<table id="life-cycle-settings">
    <tbody>
        <tr>
            <th>Настройка</th>
            <th>Описание</th>
        </tr>
        <tr>
            <td>lifeCycleSettings.stopCondition</td>
            <td>Настройка не используется</td>
        </tr>
        <tr>
            <td>lifeCycleSettings.beatDurationMs</td>
            <td>Длительность одного такта симуляции, в миллисекундах</td>
        </tr>
        <tr id="beat-count-setting">
            <td>lifeCycleSettings.beatCount</td>
            <td>Количество тактов симуляции, после которого симуляция будет остановлена</td>
        </tr>
        <tr id="threads-count-setting">
            <td>lifeCycleSettings.threadsCount</td>
            <td>Количество потоков для обработки симуляции. Так же нужно учитывать, что симуляция использует два потока на рост травы и вывод данных в консоль, которые не учитываются в этом параметре</td>
        </tr>
    </tbody>
</table>

### locationSettings

<table id="location-settings">
    <tbody>
        <tr>
            <th>Настройка</th>
            <th>Описание</th>
        </tr>
        <tr>
            <td>locationSettings.limits.{CreatureType}</td>
            <td>Задаёт максимальное количество животных вида {CreatureType} на локации</td>
        </tr>
        <tr>
            <td>locationSettings.initialCounts.{CreatureType}</td>
            <td>Задаёт стартовое количество животных вида {CreatureType} на локации, выражена в процентах от максимаьлного количества,
        соответственно, если, например, максимальное количество буйволов - 10, и эта настройка 5, то 5% от 10 меньше одного целого буйвола и ни один буйвол не появится при старте симуляции</td>
        </tr>
        <tr>
            <td>creatureSettings.{CreatureType}</td>
            <td>Задаёт настройки для вида животных {CreatureType}</td>
        </tr>
        <tr>
            <td>creatureSettings.{CreatureType}.icon</td>
            <td>Задаёт икону для отображения вида животных {CreatureType} в консоли</td>
        </tr>
        <tr>
            <td>creatureSettings.{CreatureType}.weight</td>
            <td>Задаёт вес для вида животных {CreatureType}</td>
        </tr>
        <tr id="speed-setting">
            <td>creatureSettings.{CreatureType}.speed</td>
            <td>Задаёт максимальную дальность перемещения за один ход для вида животных {CreatureType}</td>
        </tr>
        <tr id="litterCount-setting">
            <td>creatureSettings.{CreatureType}.litterCount</td>
            <td>Задаёт количество прироста при размножении за один ход для вида животных {CreatureType}</td>
        </tr>
        <tr>
            <td>creatureSettings.{CreatureType}.fullSatiety</td>
            <td>Задаёт значение еды, которое должно съесть животное, чтобы почувствовать себя сытым для вида животных {CreatureType}</td>
        </tr>
        <tr id="eating-chance-setting">
            <td>creatureSettings.{CreatureType}.eatingChance</td>
            <td>Задаёт настройки вероятности поедания в процентах для того или иного вида животных для вида животных. Т.е с какой вероятностью животное вида creatureSettings.{CreatureType} съест животное вида creatureSettings.{CreatureType}.eatingChance.{CreatureType}</td>
        </tr>
    </tbody>

</table>