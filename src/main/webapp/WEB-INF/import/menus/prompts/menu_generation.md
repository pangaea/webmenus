Generate a typical set of menus for a restaurant described as
[%S],
in XML format:
use 'location' for the restaurant,
'menu' for each menu within a location (containing 'name' property and 'index' property, starting at 0 ),
'category' for each category within each menu (containing 'name' property and 'index' property, starting at 0 ),
'item' for each food item with in 'index' property (starting at 0) used for ordering,
'name' and 'description' as text nodes under each item,
'portions' list containing
    'size' nodes each containing an
        'index' property (starting at 0) used for ordering,
        'price' property for each price for the item,
        and the innertext of the size node as a description of the item size.
'options' list containing
    'option' nodes each containing
        'index' property (starting at 0) used for ordering,
        'name' property to define the name of this option,
        'type' of either 'select' for checkboxes or 'select-one' for radio buttons,
        'choices' list containing
            'choice' nodes each containing
                'index' property (starting at 0) used for ordering,
                'price' property for the price of eacg choice,
                and the innertext of the choice node as a name of that choice