function eventHandler( eventid )
{
/*
	<enum name="question_type">
		<value text="date" code="date"/>
		<value text="fill-in" code="fill-in"/>
		<value text="multiple choice" code="multiple choice"/>
		<value text="number" code="number"/>
		<value text="paragraph" code="paragraph"/>
		<value text="yes/no" code="yes/no"/>
	</enum>
	<enum name="question_params">
		<value text="decimals" code="decimals" type="int"/>
		<value text="height" code="height" type="int"/>
		<value text="length" code="length" type="int"/>
		<value text="width" code="width" type="int"/>
	</enum>
*/
	if( eventid == 1 )
	{
		var vType = document.getElementById("workspace_questions_forms_data_type");
		if( vType != null )
		{
			var oSelect = document.getElementById("name");
			var oCount = oSelect.options.length;
			for( i = oCount-1; i >= 0; i-- )
			{
				// Keep all these types...
				if( vType.value != "extend-row" )
				{
					continue;
				}
				
				// Remove for this type
				//if( vType.value == "fill-in" &&
				//	( oSelect.options.item(i).value == "decimals" ||
				//	  oSelect.options.item(i).value == "height" ||
				//	  oSelect.options.item(i).value == "width" ) )
				//{
				//	oSelect.options.remove(i);
				//	continue;
				//}
				
				// Remove for all type except...
				if( vType.value != "fill-in" &&
					( oSelect.options.item(i).value == "length" ) )
				{
					oSelect.options.remove(i);
					continue;
				}
				if( vType.value != "fill-in (number)" &&
					( oSelect.options.item(i).value == "decimals" ) )
				{
					oSelect.options.remove(i);
					continue;
				}
				if( vType.value != "fill-in (paragraph)" &&
					( oSelect.options.item(i).value == "height" ||
					  oSelect.options.item(i).value == "width" ) )
				{
					oSelect.options.remove(i);
					continue;
				}
				if( vType.value != "muliple-choice" &&
					( oSelect.options.item(i).value == "dropdownlist" ) )
				{
					oSelect.options.remove(i);
					continue;
				}
			}
		}
		//var oOption;
		//oOption = document.createElement("OPTION");
		//oSelect.options.add(oOption);
		//oOption.innerText = "decimal";
		//oOption.value = "decimal";
	}

	return 0;
}
