import { describe, expect, test } from 'vitest';
import { day_text, days_text, money } from './format.js';

describe('money', () =>
{
    test('keeps the cents', () =>
    {
        expect(money('99000.50')).toBe('LKR 99,000.5');
    });

    test('drops a trailing .00', () =>
    {
        expect(money(150000)).toBe('LKR 150,000');
    });

    test('handles zero', () =>
    {
        expect(money(0)).toBe('LKR 0');
    });
});

describe('days_text', () =>
{
    test('uses the singular for one day', () =>
    {
        expect(days_text(1)).toBe('1 day left');
    });

    test('uses the plural otherwise', () =>
    {
        expect(days_text(0)).toBe('0 days left');
        expect(days_text(7)).toBe('7 days left');
    });
});

describe('day_text', () =>
{
    test('returns an empty string for a missing date', () =>
    {
        expect(day_text(null)).toBe('');
        expect(day_text('')).toBe('');
    });

    test('does not shift a plain date', () =>
    {
        expect(day_text('2026-10-03')).toBe('3 Oct 2026');
    });
});
