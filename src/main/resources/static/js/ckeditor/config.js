/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
    // Define changes to default configuration here. For example:

    config.language = 'es';
    // config.uiColor = '#AADC6E';

    config.extraPlugins = 'autogrow,quicktable,embed,image2,uploadimage,uploadfile,autolink,autoembed';    // autoembed

    config.removePlugins = '';

    config.allowedContent = true;	// allow all html tags and attributes

    // config.removeButtons = 'Underline,Subscript,Superscript';

    // Set the most common block elements.
    // config.format_tags = 'p;h1;h2;h3;pre';

    // Simplify the dialog windows.
    config.removeDialogTabs = 'image:advanced;link:advanced;table:advanced';

    config.height = '480px';

    config.scayt_sLang = 'es_ES'; // Corrector ortografico scayt
    config.scayt_autoStartup = true;

    // embed
    config.embed_provider = '//ckeditor.iframe.ly/api/oembed?url={url}&callback={callback}';

    //quicktable
    config.qtWidth = '100%';
    config.qtStyle = { 'border-collapse' : 'collapse' };

    config.toolbar = [
        ['Source','-','Save','NewPage','DocProps','Preview','Print','-','Templates'],
        ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo'],
        ['Find','Replace','-','SelectAll','-','SpellChecker', 'Scayt'],
        ['Form','Checkbox','Radio','TextField','Textarea','Select','Button','ImageButton','HiddenField'],
        '/',
        ['Bold','Italic','Underline','Strike','Subscript','Superscript','-','CopyFormatting','RemoveFormat' ],
        ['NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl','Language'],
        ['Link','Unlink','Anchor','-','Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Iframe'],
        '/',
        ['Styles','Format','Font','FontSize','-','TextColor','BGColor','-','Maximize', 'ShowBlocks','-','About'],
    ];

    config.toolbar = 'Full';

    config.toolbar_Full = [
        ['Source'/*,'-','Save','NewPage','DocProps','Preview','Print','-','Templates'*/],
        ['Undo','Redo','-','Cut','Copy','Paste','PasteText','PasteFromWord'],
        ['Link','Unlink',/*'Anchor',*/'-','Image','Embed',/*'Flash',*/'Table','HorizontalRule','Smiley',/*'SpecialChar','PageBreak','Iframe'*/],
        ['Find','Replace','-','SelectAll','-','SpellChecker', 'Scayt'],
        //['Form','Checkbox','Radio','TextField','Textarea','Select','Button','ImageButton','HiddenField'],
        '/',
        [/*'Styles','Format',*/'Font','FontSize','-','TextColor','BGColor'/*,'-','Maximize', 'ShowBlocks','-','About'*/],
        ['Bold','Italic','Underline',/*'Strike',*/'Subscript','Superscript','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
        ['NumberedList','BulletedList','-','Outdent','Indent','-',/*'Blockquote','CreateDiv','-',*/'CopyFormatting','RemoveFormat'/*,'-','BidiLtr','BidiRtl','Language'*/],
    ];

    config.toolbar_Basic = [
        ['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink','-','About']
    ];

    //	http://docs.ckeditor.com/#!/guide/dev_file_browse_upload
    config.filebrowserUploadUrl =  '/admin/news/upload?v=1';

};

// http://sdk.ckeditor.com/samples/devtools.html
CKEDITOR.on('dialogDefinition', function (ev) {
    // Take the dialog name and its definition from the event data.
    var dialogName = ev.data.name;
    var dialogDefinition = ev.data.definition;

    // Check if the definition is from the dialog window you are interested in (the "Link" dialog window).
    if (dialogName === 'link') {
        var informationTab = dialogDefinition.getContents('target');
        var targetField = informationTab.get('linkTargetType');
        targetField['default'] = '_blank';

        //var infoTab = dialogDefinition.getContents('info');
        //infoTab.get('linkType').style = 'display: none';
        //infoTab.get('protocol').style = 'display: none';
        //infoTab.remove('protocol');
        //infoTab.remove('linkType');

        //dialogDefinition.removeContents('target');
        //dialogDefinition.removeContents('advanced');

    }

});