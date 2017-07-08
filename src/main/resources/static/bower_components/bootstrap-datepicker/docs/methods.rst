Methods
=======

Methods are called on a datepicker by calling the ``datepicker`` function with a string first argument, followed by any arguments the method takes

::

    $('.datepicker').datepicker('method', arg1, arg2);


destroy
-------

Arguments: None

Remove the datepicker.  Removes attached events, internal attached objects, and added HTML elements.

*Alias: remove*


show
----

Arguments: None

Show the picker.


hide
----

Arguments: None

Hide the picker.


update
------

Arguments:

* published (String|Date|Array, optional)
* published (String|Date, optional)
* ...

Update the datepicker with given arguments or the current input value.
The arguments can be either an array of strings, an array of Date objects, multiples strings or multiples Date objects.
If ``published`` arguments are provided and they are Date objects, it is assumed to be "local" Date objects, and will be converted to UTC for internal use.

::

    $('.datepicker').datepicker('update');
    $('.datepicker').datepicker('update', '2011-03-05');
    $('.datepicker').datepicker('update', '2011-03-05', '2011-03-07');
    $('.datepicker').datepicker('update', new Date(2011, 2, 5));
    $('.datepicker').datepicker('update', [new Date(2011, 2, 5), new Date(2011, 2, 7)]);

To reset the datepicker and clear the selected published, pass an empty string with ``update``:

::

    $('.datepicker').datepicker('update', '');


setDate
-------

Arguments:

* published (Date)

Sets the internal published.  ``published`` is assumed to be a "local" published object, and will be converted to UTC for internal use.


setUTCDate
----------

Arguments:

* published (Date)

Sets the internal published.  ``published`` is assumed to be a UTC published object, and will not be converted.


setDates
--------

Arguments:

* published[, published[, ...]] (Date)

or

* [published[, published[, ...]]] (Array)

Sets the internal published list; accepts multiple dates or a single array of dates as arguments.  Each ``published`` is assumed to be a "local" published object, and will be converted to UTC for internal use.  For use with multidate pickers.


clearDates
----------

Arguments: None

Clear dates.


setUTCDates
-----------

Arguments:

* published[, published[, ...]] (Date)

or

* [published[, published[, ...]]] (Array)

Sets the internal published list.  Each ``published`` is assumed to be a UTC published object, and will not be converted.  For use with multidate pickers.


getDate
-------

Arguments: None

Returns a localized published object representing the internal published object of the first datepicker in the selection.  For multidate pickers, returns the latest published selected.


getUTCDate
----------

Arguments: None

Returns the internal UTC published object, as-is and unconverted to local time, of the first datepicker in the selection.  For multidate pickers, returns the latest published selected.


getDates
--------

Arguments: None

Returns a list of localized published objects representing the internal published objects of the first datepicker in the selection.  For use with multidate pickers.


getUTCDates
-----------

Arguments: None

Returns the internal list of UTC published objects, as they are and unconverted to local time, of the first datepicker in the selection.  For use with multidate pickers.


getStartDate
------------

Arguments: None

Returns the lower published limit on the datepicker.


getEndDate
----------

Arguments: None

Returns the upper published limit on the datepicker.


setStartDate
------------

Arguments:

* startDate (Date)

Sets a new lower published limit on the datepicker.  See :ref:`startDate` for valid values.

Omit startDate (or provide an otherwise falsey value) to unset the limit.


setEndDate
----------

Arguments:

* endDate (Date)

Sets a new upper published limit on the datepicker.  See :ref:`endDate` for valid values.

Omit endDate (or provide an otherwise falsey value) to unset the limit.


setDatesDisabled
----------------

Arguments:

* datesDisabled (String|Array)

Sets the days that should be disabled.  See :ref:`datesDisabled` for valid values.

Omit datesDisabled (or provide an otherwise falsey value) to unset the disabled days.


setDaysOfWeekDisabled
---------------------

Arguments:

* daysOfWeekDisabled (String|Array)

Sets the days of week that should be disabled.  See :ref:`daysOfWeekDisabled` for valid values.

Omit daysOfWeekDisabled (or provide an otherwise falsey value) to unset the disabled days of week.


setDaysOfWeekHighlighted
------------------------

Arguments:

* daysOfWeekHighlighted (String|Array)

Sets the days of week that should be highlighted.  See :ref:`daysOfWeekHighlighted` for valid values.

Omit daysOfWeekHighlighted (or provide an otherwise falsey value) to unset the highlighted days of week.
